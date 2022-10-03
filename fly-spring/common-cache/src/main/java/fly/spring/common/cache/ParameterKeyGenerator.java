package fly.spring.common.cache;

import fly.spring.common.cache.util.CacheKeyGeneratorUtil;
import org.springframework.lang.NonNull;

import java.lang.reflect.Method;

/**
 * 根据参数名和参数值生成 cache key
 *
 * @author FlyInWind
 * @date 2021/12/26
 */
public class ParameterKeyGenerator implements org.springframework.cache.interceptor.KeyGenerator {
    /**
     * @param target 目标
     * @param method 方法
     * @param params 参数个数
     * @return {@link Object}
     */
    @NonNull
    @Override
    public Object generate(@NonNull Object target, Method method, @NonNull Object... params) {
        return CacheKeyGeneratorUtil.cacheKeyGenerator(method).apply(params);
    }

}
