package fly.spring.common.cache;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.lang.NonNull;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 根据参数名和参数值生成 cache key
 *
 * @author FlyInWind
 * @date 2021/12/26
 */
public class ParameterKeyGenerator implements org.springframework.cache.interceptor.KeyGenerator {


    /** 缓存的 {@link Method#getParameters()} 按参数名排序规则 */
    protected static final LoadingCache<Method, MethodMeta> METHOD_META_CACHE = Caffeine.newBuilder()
            .expireAfterAccess(1, TimeUnit.HOURS)
            .softValues()
            .build(key -> {
                int parameterCount = key.getParameterCount();
                NameIndex[] nameIndices = new NameIndex[parameterCount];
                Parameter[] parameters = key.getParameters();
                for (int i = 0; i < parameterCount; i++) {
                    nameIndices[i] = new NameIndex(parameters[i].getName(), i);
                }
                Arrays.sort(nameIndices, Comparator.comparing(NameIndex::getKey));
                return new MethodMeta(key, nameIndices);
            });

    /**
     * @param target 目标
     * @param method 方法
     * @param params 参数个数
     * @return {@link Object}
     */
    @NonNull
    @Override
    public Object generate(@NonNull Object target, Method method, @NonNull Object... params) {
        // 参数数量为0或1时可以直接返回
        int parameterCount = method.getParameterCount();
        if (parameterCount == 0) {
            return Collections.emptyMap();
        } else if (parameterCount == 1) {
            return Collections.singletonMap(method.getParameters()[0].getName(), params[0]);
        }

        // 参数数量多于1，根据参数名排序
        MethodMeta methodMeta = METHOD_META_CACHE.get(method);
        NameIndex[] sortedNameIndexes = Objects.requireNonNull(methodMeta).sortedNameIndexes;

        int mapSize = (int) (parameterCount / 0.75) + 1;
        Map<String, Object> result = new LinkedHashMap<>(mapSize);

        for (int i = 0; i < parameterCount; i++) {
            NameIndex nameIndex = sortedNameIndexes[i];
            int idx = nameIndex.value;
            Object param = params[idx];
            if (param != null) {
                result.put(nameIndex.key, param);
            }
        }
        return result;
    }

    /**
     * 用于存储 {@link Method#getParameters()} 排序后下标的临时对象
     *
     * @author FlyInWind
     * @since 2022/08/08
     */
    @Getter
    @AllArgsConstructor
    protected static class NameIndex {
        private final String key;
        private final int value;
    }

    /**
     * 方法及其参数的排序
     *
     * @author FlyInWind
     * @since 2022/08/08
     */
    @Getter
    @AllArgsConstructor
    protected static class MethodMeta {
        private final Method method;
        private final NameIndex[] sortedNameIndexes;
    }
}
