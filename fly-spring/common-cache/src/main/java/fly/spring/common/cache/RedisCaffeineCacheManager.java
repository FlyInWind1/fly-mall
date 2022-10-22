package fly.spring.common.cache;

import com.pig4cloud.plugin.cache.properties.CacheConfigProperties;
import com.pig4cloud.plugin.cache.support.RedisCaffeineCache;
import org.springframework.data.redis.core.RedisTemplate;

public class RedisCaffeineCacheManager extends com.pig4cloud.plugin.cache.support.RedisCaffeineCacheManager {
    public RedisCaffeineCacheManager(CacheConfigProperties cacheConfigProperties, RedisTemplate<Object, Object> stringKeyRedisTemplate) {
        super(cacheConfigProperties, stringKeyRedisTemplate);
    }

    @Override
    public RedisCaffeineCache createCache(String name) {
        return new RedisCaffeineCache(name, getStringKeyRedisTemplate(), caffeineCache(), getCacheConfigProperties());
    }

}
