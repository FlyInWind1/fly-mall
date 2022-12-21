package fly.spring.common.cache.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Data
@Primary
@Component
@ConfigurationProperties(prefix = "spring.cache.multi")
public class CacheConfigProperties extends com.pig4cloud.plugin.cache.properties.CacheConfigProperties {

    /**
     * 是否将 bean 转为不可修改的
     */
    private boolean defaultUnmodifiableBean = false;

    @NestedConfigurationProperty
    private RedisConfigProp redis = new RedisConfigProp();

}
