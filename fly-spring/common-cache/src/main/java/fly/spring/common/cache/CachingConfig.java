package fly.spring.common.cache;

import com.pig4cloud.plugin.cache.support.RedisCaffeineCacheManagerCustomizer;
import com.pig4cloud.plugin.cache.support.ServerIdGenerator;
import fly.spring.common.cache.properties.CacheConfigProperties;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author FlyInWind
 * @date 2021/12/26
 */
@AutoConfiguration
@AllArgsConstructor
@EnableConfigurationProperties(CacheConfigProperties.class)
public class CachingConfig implements CachingConfigurer {
    private final ParameterKeyGenerator keyGenerator;

    @Override
    public KeyGenerator keyGenerator() {
        return keyGenerator;
    }

    @Bean
    public RedisCaffeineCacheManager cacheManager(CacheConfigProperties cacheConfigProperties,
                                                  @Qualifier("stringKeyRedisTemplate") RedisTemplate<Object, Object> stringKeyRedisTemplate,
                                                  ObjectProvider<RedisCaffeineCacheManagerCustomizer> cacheManagerCustomizers,
                                                  ObjectProvider<ServerIdGenerator> serverIdGenerators) {
        Object serverId = cacheConfigProperties.getServerId();
        if (serverId == null || "".equals(serverId)) {
            serverIdGenerators
                    .ifAvailable(serverIdGenerator -> cacheConfigProperties.setServerId(serverIdGenerator.get()));
        }
        RedisCaffeineCacheManager cacheManager = new RedisCaffeineCacheManager(cacheConfigProperties,
                stringKeyRedisTemplate);
        cacheManagerCustomizers.orderedStream().forEach(customizer -> customizer.customize(cacheManager));
        return cacheManager;
    }
}
