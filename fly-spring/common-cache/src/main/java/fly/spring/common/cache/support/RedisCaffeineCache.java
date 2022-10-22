package fly.spring.common.cache.support;

import com.github.benmanes.caffeine.cache.Cache;
import fly.spring.common.cache.properties.CacheConfigProperties;
import fly.spring.common.core.util.UnmodifiableUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.Duration;

/**
 * @author FlyInWind
 */
@Slf4j
@Getter
@SuppressWarnings("unchecked")
public class RedisCaffeineCache extends com.pig4cloud.plugin.cache.support.RedisCaffeineCache implements Cache<Object, Object> {
    private final boolean enableRedis;

    private final boolean unmodifiableBean;

    public RedisCaffeineCache(String name, RedisTemplate<Object, Object> stringKeyRedisTemplate, Cache<Object, Object> caffeineCache, CacheConfigProperties cacheConfigProperties) {
        super(name, stringKeyRedisTemplate, caffeineCache, cacheConfigProperties);
        this.enableRedis = !cacheConfigProperties.getRedis().getDisables().contains(name);
        this.unmodifiableBean = cacheConfigProperties.isDefaultUnmodifiableBean();
    }

    @Override
    protected void setCaffeineValue(Object key, Object value) {
        if (unmodifiableBean) {
            value = UnmodifiableUtil.unmodifiable(value);
        }
        super.setCaffeineValue(key, value);
    }

    @Override
    protected void setRedisValue(Object key, Object value, Duration expire) {
        if (enableRedis) {
            super.setRedisValue(key, value, expire);
        }
    }

    @Override
    protected Object getRedisValue(Object key) {
        if (enableRedis) {
            return super.getRedisValue(key);
        }
        return null;
    }

}
