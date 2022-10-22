package fly.spring.common.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.pig4cloud.plugin.cache.properties.CacheConfigProperties;
import org.springframework.data.redis.cache.BatchStrategies;
import org.springframework.data.redis.cache.BatchStrategy;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

public class RedisCaffeineCache extends com.pig4cloud.plugin.cache.support.RedisCaffeineCache {
    BatchStrategy batchStrategy = BatchStrategies.scan(Short.MAX_VALUE);

    public RedisCaffeineCache(String name, RedisTemplate<Object, Object> stringKeyRedisTemplate, Cache<Object, Object> caffeineCache, CacheConfigProperties cacheConfigProperties) {
        super(name, stringKeyRedisTemplate, caffeineCache, cacheConfigProperties);
    }

    public void clear(String pattern) {
        byte[] patternBytes = serializeKey(pattern);
        getStringKeyRedisTemplate().execute((RedisCallback<Object>) connection ->
                batchStrategy.cleanCache(connection, getName(), patternBytes));

    }


    @SuppressWarnings("unchecked")
    public byte[] serializeKey(Object key) {
        RedisSerializer<Object> keySerializer = (RedisSerializer<Object>) getStringKeyRedisTemplate().getKeySerializer();
        return keySerializer.serialize(key);
    }
}
