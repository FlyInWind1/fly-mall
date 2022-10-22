package fly.spring.common.cdc.spring.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@Data
@ConfigurationProperties("debezium")
public class DebeziumProperties {

    private Map<String, String> baseProperties;

}
