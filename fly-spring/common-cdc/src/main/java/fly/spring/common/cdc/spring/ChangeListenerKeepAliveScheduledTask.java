package fly.spring.common.cdc.spring;

import fly.spring.common.cdc.listener.AbstractEmbeddedDebeziumChangeListener;
import fly.spring.common.cdc.spring.interfaces.PgSlotsUsedChecker;
import fly.spring.common.cdc.spring.properties.DebeziumProperties;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

@Slf4j
@AllArgsConstructor
@EnableConfigurationProperties(DebeziumProperties.class)
public class ChangeListenerKeepAliveScheduledTask {
    private final DataSourceProperties dataSourceProperties;
    private final DebeziumProperties debeziumProperties;
    private final ObjectProvider<AbstractEmbeddedDebeziumChangeListener<?>> changeListeners;
    private final ObjectProvider<PgSlotsUsedChecker> pgSlotsUsedCheckerProvider;


    /**
     * 10 分钟运行一次
     */
    @Scheduled(fixedDelay = 10, timeUnit = TimeUnit.MINUTES, initialDelay = 0)
    public void keepAlive() {
        Map<String, String> baseProperties = debeziumProperties.getBaseProperties();
        Map<String, String> datasourceProperties = new HashMap<>();

        String url = dataSourceProperties.getUrl().replace("jdbc:postgresql://", "");
        int index = url.lastIndexOf("/");
        String database = url.substring(index + 1);
        url = url.substring(0, index);
        index = url.indexOf(":");
        if (index != -1) {
            datasourceProperties.put("database.port", url.substring(index + 1));
            url = url.substring(0, index);
        }
        String hostname = url;

        datasourceProperties.put("database.hostname", hostname);
        datasourceProperties.put("database.dbname", database);
        datasourceProperties.put("database.password", dataSourceProperties.getPassword());
        datasourceProperties.put("database.user", dataSourceProperties.getUsername());

        PgSlotsUsedChecker slotsUsedChecker = pgSlotsUsedCheckerProvider.getIfAvailable();

        for (AbstractEmbeddedDebeziumChangeListener<?> changeListener : changeListeners) {
            try {
                Properties listenerDebeziumProperties = changeListener.getDebeziumProperties();
                baseProperties.forEach((k, v) -> {
                    if (!listenerDebeziumProperties.contains(k)) {
                        listenerDebeziumProperties.setProperty(k, v);
                    }
                });
                datasourceProperties.forEach((k, v) -> {
                    if (!listenerDebeziumProperties.contains(k)) {
                        listenerDebeziumProperties.setProperty(k, v);
                    }
                });
                if (slotsUsedChecker != null) {
                    String slotName = listenerDebeziumProperties.getProperty("slot.name");
                    if (!slotsUsedChecker.test(slotName)) {
                        // 如果 slot 槽没有被占用，才启动 Listener
                        changeListener.start();
                    }
                } else {
                    changeListener.start();
                }
            } catch (Exception e) {
                Class<?> targetClass = AopUtils.getTargetClass(changeListener);
                log.error("启动变化监控 {} 时发生异常", targetClass.getCanonicalName(), e);
            }
        }
    }
}
