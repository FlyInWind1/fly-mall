package fly.spring.common.cdc.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import fly.spring.common.cdc.domain.Payload;
import fly.spring.common.cdc.domain.TableTypeMeta;
import fly.spring.common.cdc.interfaces.ChangeHandler;
import fly.spring.common.cdc.properties.EmbeddedDebeziumChangeListenerProperties;
import fly.spring.common.cdc.type.TypeReference;
import fly.spring.common.cdc.util.MybatisPlusUtil;
import fly.spring.common.cdc.util.StringUtil;
import fly.spring.common.cdc.util.TableIdUtil;
import io.debezium.engine.ChangeEvent;
import io.debezium.engine.DebeziumEngine;
import io.debezium.engine.format.Json;
import io.debezium.relational.TableId;
import io.debezium.util.Strings;
import lombok.Getter;
import lombok.SneakyThrows;
import org.apache.kafka.common.utils.ThreadUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 基于嵌入式 debezium 的表变化监听器
 *
 * @author fly.spring.common
 * @since 2022/08/07
 */
public abstract class AbstractEmbeddedDebeziumChangeListener<T> extends TypeReference<T>
        implements Runnable, Closeable, ChangeHandler<T>
        , DebeziumEngine.ChangeConsumer<ChangeEvent<String, String>> {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    //---------- 泛型 T

    @Getter
    protected final JavaType entityType;

    public Class<? extends T> getEntityClass() {
        //noinspection unchecked
        return (Class<? extends T>) entityType.getRawClass();
    }

    @Getter
    protected final JavaType payloadType;

    //---------- json 序列化

    @Getter
    protected final ObjectMapper objectMapper;

    public TypeFactory getTypeFactory() {
        return objectMapper.getTypeFactory();
    }

    //---------- debezium

    @Getter
    protected final Properties debeziumProperties;
    @Getter
    protected DebeziumEngine<ChangeEvent<String, String>> debeziumEngine;

    @Getter
    protected final ExecutorService executorService = createExecutorService();

    //---------- 配置
    @Getter
    protected final EmbeddedDebeziumChangeListenerProperties listenerProperties;

    @Getter
    protected final Map<TableId, TableTypeMeta> tableNameTypeMetaMap;

    @SneakyThrows(IOException.class)
    protected AbstractEmbeddedDebeziumChangeListener(EmbeddedDebeziumChangeListenerProperties properties) {
        this.listenerProperties = properties;
        objectMapper = properties.getObjectMapper();

        JavaType configuredEntityType = properties.getEntityType();
        if (configuredEntityType == null || getParameterType().equals(configuredEntityType)) {
            entityType = getParameterType();
        } else {
            if (!getParameterType().isTypeOrSuperTypeOf(configuredEntityType.getRawClass())) {
                String msg = String.format("配置的 entityType %s 不是泛型类型 %s 的子类",
                        configuredEntityType.toCanonical(), getParameterType().toCanonical());
                throw new IllegalArgumentException(msg);
            }
            entityType = configuredEntityType;
        }
        payloadType = getTypeFactory().constructParametricType(Payload.class, entityType);

        debeziumProperties = properties.getDebeziumProperties();

        // 表及其对应的实体类
        Map<String, JavaType> tableNameTypeMap = properties.getTableNameTypeMap();
        if (tableNameTypeMap.isEmpty()) {
            // 没有配置表名的话，通过 mybatis-plus 读取
            String tableName = MybatisPlusUtil.findTableByMyBatisPlus(getEntityClass());
            if (Strings.isNullOrBlank(tableName)) {
                throw new IllegalArgumentException("请配置 tableName");
            }
            tableNameTypeMap.put(tableName, null);
        }

        // 处理表名，添加 schema 信息
        Map<TableId, TableTypeMeta> map = new LinkedHashMap<>();
        tableNameTypeMap.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> {
                    JavaType type = entry.getValue();
                    JavaType payloadT;
                    if (type == null) {
                        type = getEntityType();
                        payloadT = getPayloadType();
                    } else {
                        if (!getParameterType().isTypeOrSuperTypeOf(type.getRawClass())) {
                            throw new IllegalArgumentException(
                                    String.format("表 %s 的实体类型 %s 不是泛型 %s 的子类",
                                            entry.getKey(), type.toCanonical(), entityType.toCanonical()));
                        }
                        payloadT = getTypeFactory().constructParametricType(Payload.class, type);
                    }
                    TableId tableId = TableIdUtil.parseTableIdWithPublicPrefix(entry.getKey());
                    TableTypeMeta meta = TableTypeMeta.builder()
                            .tableId(tableId)
                            .entityType(type)
                            .payloadType(payloadT)
                            .build();
                    map.put(tableId, meta);
                });
        tableNameTypeMetaMap = Collections.unmodifiableMap(map);

        // 配置 payloadType 解析器
        if (tableNameTypeMap.size() == 1) {
            // 如果只有一张表，直接返回第一个 JavaType
            TableTypeMeta tableTypeMeta = map.values().stream()
                    .findFirst().orElseThrow(IllegalStateException::new);
            JavaType payloadT = tableTypeMeta.getPayloadType();
            payloadTypeParser = p -> payloadT;
        } else {
            // 有多长表，通过表名寻找 JavaType
            payloadTypeParser = p -> {
                JsonNode sourceNode = p.get("source");
                String schema = sourceNode.get("schema").asText();
                String table = sourceNode.get("table").asText();
                TableId tableId = new TableId(null, schema, table);
                TableTypeMeta meta = map.get(tableId);
                return meta.getPayloadType();
            };
        }

        // 准备 debezium 的配置
        debeziumProperties.setProperty("table.include.list", createTableIncludeList());
        // 通过需要监听的表名设置 publication.name，仅监听配置了的表
        String publicationName = properties.getPublicationName();
        if (Strings.isNullOrBlank(publicationName)) {
            publicationName = createPublicationName();
        }
        debeziumProperties.setProperty("publication.name", publicationName);
        // 通过类名设置 slot.name
        String slotName = properties.getSlotName();
        if (Strings.isNullOrEmpty(slotName)) {
            slotName = debeziumProperties.getProperty("slot.name");
        }
        if (Strings.isNullOrEmpty(slotName)) {
            String className = getClass().getSimpleName();
            className = StringUtil.camelToUnderline(className);
            slotName = className + "_slot";
            log.warn("没有配置 slotName，将使用由类型转下划线而成的 {} 作为 slotName", slotName);
        }
        debeziumProperties.setProperty("slot.name", slotName);
        debeziumProperties.putIfAbsent("database.server.name", slotName);
        debeziumProperties.putIfAbsent("name", slotName);
        debeziumProperties.putIfAbsent("topic.prefix", slotName);

        // 配置默认的 FileOffsetBackingStore
        String dataDir = "./debezium/" + slotName;
        Path dataDirPath = Paths.get(dataDir);
        String offsetStorage = debeziumProperties.getProperty("offset.storage");
        if (Strings.isNullOrEmpty(offsetStorage)) {
            debeziumProperties.setProperty("offset.storage", "org.apache.kafka.connect.storage.FileOffsetBackingStore");
            String fileName = dataDir + "/offsets.dat";
            debeziumProperties.putIfAbsent("offset.storage.file.filename", fileName);
        }
        if (!Strings.isNullOrEmpty(debeziumProperties.getProperty("offset.storage.file.filename"))) {
            Files.createDirectories(Paths.get(debeziumProperties.getProperty("offset.storage.file.filename")).getParent());
        }
        // 配置默认的FileDatabaseHistory
        String databaseHistory = debeziumProperties.getProperty("database.history");
        if (Strings.isNullOrEmpty(databaseHistory)) {
            debeziumProperties.setProperty("database.history", "io.debezium.relational.history.FileDatabaseHistory");
            String fileName = dataDir + "/dbhistory.dat";
            debeziumProperties.putIfAbsent("database.history.file.filename", fileName);
            Files.createDirectories(dataDirPath);
        }
        if (!Strings.isNullOrEmpty(debeziumProperties.getProperty("database.history.file.filename"))) {
            Files.createDirectories(Paths.get(debeziumProperties.getProperty("database.history.file.filename")).getParent());
        }
    }

    protected ExecutorService createExecutorService() {
        ThreadFactory threadFactory = ThreadUtils.createThreadFactory(getClass().getSimpleName() + "-%d", false);
        return Executors.newSingleThreadExecutor(threadFactory);
    }

    //---------- debeziumProperties 配置相关

    protected String createTableIncludeList() {
        return tableNameTypeMetaMap.keySet().stream()
                .map(TableId::toString)
                .collect(Collectors.joining(","));
    }

    protected String createPublicationName() {
        String tableNameList = tableNameTypeMetaMap.keySet().stream()
                .map(TableIdUtil::getTableNameWithOutPublicPrefix)
                .collect(Collectors.joining("_"));
        return tableNameList + "_publication";
    }

    //---------- 反序列化相关

    /**
     * 反序列化实体类
     *
     * @param records 记录
     * @return {@link List}<{@link Payload}<{@link T}>>
     */
    @SneakyThrows(JsonProcessingException.class)
    public List<Payload<T>> deserializeChangeEventList(List<ChangeEvent<String, String>> records) {
        List<Payload<T>> objects = new ArrayList<>(records.size());
        for (ChangeEvent<String, String> rec : records) {
            String recValue = rec.value();
            if (recValue == null) {
                // 物理删除之后，可能会出现 value 为 null 的数据
                continue;
            }
            JsonNode jsonNode = objectMapper.readTree(recValue);
            JsonNode payload = jsonNode.get("payload");
            Payload<T> payloadObj = objectMapper.treeToValue(payload, parsePayloadType(payload));
            objects.add(payloadObj);
        }
        return objects;
    }

    protected final Function<JsonNode, JavaType> payloadTypeParser;

    public JavaType parsePayloadType(JsonNode payload) {
        return payloadTypeParser.apply(payload);
    }

    //---------- 执行业务数据相关

    @Override
    public void handleBatch(List<ChangeEvent<String, String>> records, DebeziumEngine.RecordCommitter<ChangeEvent<String, String>> committer) throws InterruptedException {
        List<Payload<T>> payloads = deserializeChangeEventList(records);
        handleEntityBatch(payloads);
        for (ChangeEvent<String, String> rec : records) {
            committer.markProcessed(rec);
        }
        committer.markBatchFinished();
    }

    //---------- 启动停止

    public void start() {
        executorService.submit(this);
    }

    @Override
    public void run() {
        try {
            synchronized (this) {
                if (debeziumEngine != null) {
                    return;
                }
                // 创建 debezium engine
                debeziumEngine = DebeziumEngine.create(Json.class)
                        .using(debeziumProperties)
                        .notifying(this)
                        .build();
            }
            debeziumEngine.run();
        } finally {
            synchronized (this) {
                debeziumEngine = null;
            }
        }
    }

    @Override
    public void close() throws IOException {
        if (debeziumEngine != null) {
            debeziumEngine.close();
        }
        executorService.shutdown();
    }
}
