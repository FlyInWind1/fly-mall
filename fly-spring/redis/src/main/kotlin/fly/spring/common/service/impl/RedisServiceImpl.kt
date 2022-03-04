package fly.spring.common.service.impl

import fly.spring.common.service.RedisService
import org.springframework.data.redis.core.ReactiveHashOperations
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.core.ReactiveValueOperations
import org.springframework.stereotype.Service
import java.time.Duration

@Service
@Suppress("UNCHECKED_CAST")
class RedisServiceImpl(
        private val template: ReactiveRedisTemplate<String, Any>,
        private val valueOperation: ReactiveValueOperations<String, Any>,
        private val hashOperations: ReactiveHashOperations<String, String, Any>
) : RedisService {

    override fun set(key: String, value: Any): Boolean {
        return valueOperation.set(key, value).block()!!
    }

    override fun set(key: String, value: Any, timeout: Long): Boolean {
        return valueOperation.set(key, value, Duration.ofSeconds(timeout)).block()!!
    }

    override fun del(key: String): Boolean {
        return valueOperation.delete(key).block()!!
    }

    override fun <T> get(key: String): T? {
        return valueOperation.get(key).block() as T?
    }

    override fun expire(key: String, time: Long): Boolean {
        return template.expire(key, Duration.ofSeconds(time)).block()!!
    }


    override fun getExpire(key: String): Long? {
        return template.getExpire(key).block()?.seconds
    }


    override fun hasKey(key: String): Boolean {
        return template.hasKey(key).block()!!
    }

    override fun hSet(key: String, hashKey: String, value: Any): Boolean {
        return hashOperations.put(key, hashKey, value).block()!!
    }


    override fun <T> hGet(key: String, hashKey: String): T? {
        return hashOperations.get(key, hashKey).block() as T?
    }

    override fun template(): ReactiveRedisTemplate<String, Any> {
        return template
    }
}