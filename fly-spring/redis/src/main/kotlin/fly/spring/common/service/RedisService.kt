package fly.spring.common.service

import org.springframework.data.redis.core.ReactiveRedisTemplate

/**
 * option redis
 */
interface RedisService {

    /**
     * get value of a key
     */
    fun set(key: String, value: Any): Boolean

    /**
     * get value of a key with timeout, unit is second
     */
    fun set(key: String, value: Any, timeout: Long): Boolean

    /**
     * delete a key
     */
    fun del(key: String): Boolean

    /**
     * get value of a key
     */
    fun <T> get(key: String): T?

    /**
     * set timeout for a key
     */
    fun expire(key: String, time: Long): Boolean

    /**
     * get timeout of a key
     */
    fun getExpire(key: String): Long?

    /**
     * check key is exist
     */
    fun hasKey(key: String): Boolean

    /**
     * set value for key in a hash
     */
    fun hSet(key: String, hashKey: String, value: Any): Boolean

    /**
     * get value in a hash by key
     */
    fun <T> hGet(key: String, hashKey: String): T?

    /**
     * return redisTemplate
     */
    fun template(): ReactiveRedisTemplate<String, Any>

}