package fly.spring.common.config

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory
import org.springframework.data.redis.core.ReactiveHashOperations
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.core.ReactiveValueOperations
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.RedisSerializer

@Configuration(proxyBeanMethods = false)
class RedisConfiguration {

    @Bean
    fun reactiveRedisTemplate(
        redisConnectionFactory: ReactiveRedisConnectionFactory,
        @Qualifier(JacksonConfiguration.OBJECT_MAPPER_DEFAULT_TYPING) mapper: ObjectMapper
    ): ReactiveRedisTemplate<String, Any> {
        val stringRedisSerializer = RedisSerializer.string()
        val jsonRedisSerializer = GenericJackson2JsonRedisSerializer(mapper)
        val serializationContext = RedisSerializationContext.newSerializationContext<String, Any>()
            .key(stringRedisSerializer)
            .value(jsonRedisSerializer)
            .hashKey(stringRedisSerializer)
            .hashValue(jsonRedisSerializer)
            .build()
        return ReactiveRedisTemplate(redisConnectionFactory, serializationContext)
    }

    @Bean
    fun valueOperations(template: ReactiveRedisTemplate<String, Any>): ReactiveValueOperations<String, Any> {
        return template.opsForValue()
    }

    @Bean
    fun hashOperations(template: ReactiveRedisTemplate<String, Any>): ReactiveHashOperations<String, String, Any> {
        return template.opsForHash()
    }
}
