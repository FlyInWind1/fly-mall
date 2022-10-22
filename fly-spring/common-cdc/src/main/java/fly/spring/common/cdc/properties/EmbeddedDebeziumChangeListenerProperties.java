package fly.spring.common.cdc.properties;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import fly.spring.common.cdc.util.JsonUtil;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Data
@Accessors(chain = true)
public class EmbeddedDebeziumChangeListenerProperties {

    private Properties debeziumProperties = new Properties();

    /** debezium属性，详见 <a href="https://debezium.io/documentation/reference/1.9/connectors/postgresql.html#postgresql-connector-properties">官方文档</a> */
    public EmbeddedDebeziumChangeListenerProperties setDebeziumProp(String key, String value) {
        debeziumProperties.setProperty(key, value);
        return this;
    }

    private ObjectMapper objectMapper = JsonUtil.objectMapper;

    /**
     * 默认通过 tableNames 构建
     * <br>
     * 例如 test_1,table_2 -> test_1-table_2-publication
     */
    private String publicationName;

    /** postgresql 槽名称，关键字段，用于区分当前服务 */
    private String slotName;

    /** 实体类型 */
    private JavaType entityType;

    public EmbeddedDebeziumChangeListenerProperties setEntityType(Class<?> clazz) {
        this.entityType = getObjectMapper().constructType(clazz);
        return this;
    }

    /** 表名，及其对应的实体类 */
    private Map<String, JavaType> tableNameTypeMap = new HashMap<>();

    public EmbeddedDebeziumChangeListenerProperties addTable(String tableName) {
        tableNameTypeMap.put(tableName, null);
        return this;
    }

    public EmbeddedDebeziumChangeListenerProperties addTable(String tableName, Class<?> clazz) {
        JavaType javaType = objectMapper.getTypeFactory().constructType(clazz);
        return addTable(tableName, javaType);
    }

    public EmbeddedDebeziumChangeListenerProperties addTable(String tableName, JavaType javaType) {
        tableNameTypeMap.put(tableName, javaType);
        return this;
    }

}
