package fly.spring.common.config

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.ReactiveHashOperations
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.core.ReactiveValueOperations
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.RedisSerializer

@AutoConfiguration
class RedisConfiguration {

    @Bean
    fun keyRedisSerializer(): RedisSerializer<String> {
        return RedisSerializer.string()
    }

    @Bean
    fun valueRedisSerializer(
        @Qualifier(JacksonConfiguration.OBJECT_MAPPER_DEFAULT_TYPING) mapper: ObjectMapper
    ): RedisSerializer<Any> {
        return GenericJackson2JsonRedisSerializer(mapper)
    }

    @Bean
    fun redisSerializationContext(
        @Qualifier("keyRedisSerializer") keyRedisSerializer: RedisSerializer<String>,
        @Qualifier("valueRedisSerializer") valueRedisSerializer: RedisSerializer<Any>,
    ): RedisSerializationContext<String, Any> {
        return RedisSerializationContext.newSerializationContext<String, Any>()
            .key(keyRedisSerializer)
            .value(valueRedisSerializer)
            .hashKey(keyRedisSerializer)
            .hashValue(valueRedisSerializer)
            .build()
    }

    @Bean
    fun reactiveRedisTemplate(
        redisConnectionFactory: ReactiveRedisConnectionFactory,
        redisSerializationContext: RedisSerializationContext<String, Any>,
    ): ReactiveRedisTemplate<String, Any> {
        return ReactiveRedisTemplate(redisConnectionFactory, redisSerializationContext)
    }

    @Bean
    fun reactiveValueOperations(template: ReactiveRedisTemplate<String, Any>): ReactiveValueOperations<String, Any> {
        return template.opsForValue()
    }

    @Bean
    fun reactiveHashOperations(template: ReactiveRedisTemplate<String, Any>): ReactiveHashOperations<String, String, Any> {
        return template.opsForHash()
    }

    @Bean("redisTemplate", "stringKeyRedisTemplate")
    fun redisTemplate(
        redisConnectionFactory: RedisConnectionFactory,
        @Qualifier("keyRedisSerializer") keyRedisSerializer: RedisSerializer<String>,
        @Qualifier("valueRedisSerializer") valueRedisSerializer: RedisSerializer<Any>,
    ): RedisTemplate<String, *> {
        val redisTemplate = RedisTemplate<String, Any>()
        redisTemplate.setConnectionFactory(redisConnectionFactory)
        redisTemplate.keySerializer = keyRedisSerializer
        redisTemplate.valueSerializer = valueRedisSerializer
        redisTemplate.hashKeySerializer = keyRedisSerializer
        redisTemplate.hashValueSerializer = valueRedisSerializer
        return redisTemplate
    }

}
