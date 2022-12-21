package fly.spring.common.cache.support;

import fly.spring.common.cache.properties.CacheConfigProperties;
import org.springframework.data.redis.core.RedisTemplate;

public class RedisCaffeineCacheManager extends com.pig4cloud.plugin.cache.support.RedisCaffeineCacheManager implements CacheManager {
    public RedisCaffeineCacheManager(CacheConfigProperties cacheConfigProperties, RedisTemplate<Object, Object> stringKeyRedisTemplate) {
        super(cacheConfigProperties, stringKeyRedisTemplate);
    }

    @Override
    public RedisCaffeineCache createCache(String name) {
        return new RedisCaffeineCache(name, getStringKeyRedisTemplate(), caffeineCache(name), getCacheConfigProperties());
    }

    @Override
    public CacheConfigProperties getCacheConfigProperties() {
        return (CacheConfigProperties) super.getCacheConfigProperties();
    }

}
