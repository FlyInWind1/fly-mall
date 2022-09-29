package fly.spring.common.cache.util;

import fly.spring.common.bean.TestClass1;
import fly.spring.common.bean.TestClass2;
import org.apache.ibatis.annotations.Param;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CacheKeyGeneratorUtilTest {

    @Test
    void cacheKeyGenerator() {
        Function<TestClass1, Object> function1 = CacheKeyGeneratorUtil.cacheKeyGenerator(TestClass1::getA1);
        TestClass1 o1 = new TestClass1();
        o1.setA1("a1");
        Object key = function1.apply(o1);
        assertEquals("a1=a1", key);

        Function<TestClass2, Object> function2 = CacheKeyGeneratorUtil.cacheKeyGenerator(TestClass2::getA2, TestClass2::getA1);
        TestClass2 o2 = new TestClass2();
        o2.setA1("a1");
        o2.setA2(1);
        key = function2.apply(o2);
        assertEquals("a1=a1:a2=1", key);
    }

    static final Function<Object[], Object> testCacheKey = CacheKeyGeneratorUtil.cacheKeyGenerator(CacheKeyGeneratorUtilTest.class, "testCacheKey");

    static Object testCacheKey(@Param("a2") Integer a2, @Param("a1") String a1) {
        return testCacheKey.apply(new Object[]{a2, a1});
    }

    @Test
    void cacheKeyGeneratorMethod() {
        Object key = testCacheKey(1, "a1");
        assertEquals("a1=a1:a2=1", key);
    }
}
