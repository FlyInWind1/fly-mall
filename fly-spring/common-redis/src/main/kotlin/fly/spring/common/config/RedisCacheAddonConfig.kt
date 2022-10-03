package fly.spring.common.config

import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.data.redis.cache.RedisCacheConfiguration
import org.springframework.data.redis.serializer.RedisSerializationContext

@AutoConfiguration
@ConditionalOnProperty("spring.cache.type", havingValue = "redis")
class RedisCacheAddonConfig {

    @Bean
    fun redisCacheConfiguration(
        redisSerializationContext: RedisSerializationContext<String, Any>,
    ): RedisCacheConfiguration {
        return RedisCacheConfiguration.defaultCacheConfig()
            .serializeKeysWith(redisSerializationContext.keySerializationPair)
            .serializeValuesWith(redisSerializationContext.valueSerializationPair)
    }

}
