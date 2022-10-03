package fly.spring.common.cache

import com.pig4cloud.plugin.cache.support.RedisCaffeineCache
import fly.mall.AdminApplication
import fly.spring.common.bean.TestClass2
import fly.spring.common.cache.util.CacheKeyGeneratorUtil
import org.apache.ibatis.annotations.Param
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cache.Cache
import org.springframework.cache.CacheManager
import javax.annotation.Resource
import kotlin.test.assertNull

@SpringBootTest(classes = [AdminApplication::class])
class CacheTestApplication {

    companion object {
        val genTestCacheKeyGenerator =
            CacheKeyGeneratorUtil.cacheKeyGenerator(CacheTestApplication::class.java, "genTestCacheKey")
    }

    private lateinit var cache: Cache

    @Resource
    fun setCache(cacheManager: CacheManager) {
        cache = cacheManager.getCache("cache:test")!!
    }

    fun genTestCacheKey(@Param("str1") str1: String, @Param("int1") int1: Int): Any {
        return genTestCacheKeyGenerator.apply(arrayOf(str1, int1))
    }

    @Test
    fun test1() {
        val key = genTestCacheKey("s1", 1)
        assertEquals("int1=1:str1=s1", key)
        cache.evict(key)
        val o1 = TestClass2().apply {
            a1 = "s1"
        }

        cache.put(key, o1)
        var o2 = cache.get(key)?.get()
        assertEquals(o1, o2)
        cache.evict(key)
        o2 = cache.get(key)
        assertNull(o2)

        cache.put(key, null)
        o2 = cache.get(key)?.get()
        assertNull(o2)
        (cache as RedisCaffeineCache).clearLocal(null)
        o2 = cache.get(key)?.get()
        assertNull(o2)
    }

}
