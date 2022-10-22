package fly.spring.common.cache.properties;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class RedisConfigProp extends com.pig4cloud.plugin.cache.properties.RedisConfigProp {

    /**
     * 需要关闭redis缓存的缓存名
     */
    private Set<String> disables = new HashSet<>();

}
