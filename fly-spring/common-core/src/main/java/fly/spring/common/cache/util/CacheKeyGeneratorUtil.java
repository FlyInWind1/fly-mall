package fly.spring.common.cache.util;

import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.core.toolkit.LambdaUtils;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import fly.spring.common.core.util.ReflectUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.ibatis.reflection.property.PropertyNamer;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.AbstractMap.SimpleEntry;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 缓存键生成器跑龙套
 *
 * @author FlyInWind
 * @since 2022/10/03
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CacheKeyGeneratorUtil {
    public static final String COLON = ":";
    public static final String EQUAL = "=";

    /**
     * create a cache key generator
     *
     * <pre>{@code
     * Function<Domain, Object> cacheKeyGenerator = CacheKeyGeneratorUtil
     *      .cacheKeyGenerator(Domain::getProp1, Domain::getProp2);
     * Domain domain = new Domain();
     * domain.setProp1("p1");
     * domain.setProp2("p2");
     * Object cacheKey = cacheKeyGenerator.apply(domain);
     * // cacheKey = "prop1=p1:prop2=p2"
     * }</pre>
     *
     * @param functions sFunctions
     * @return {@link Function}<{@link T}, {@link Object}>
     */
    @SafeVarargs
    public static <T> Function<T, Object> cacheKeyGenerator(SFunction<T, ?>... functions) {
        Assert.notEmpty(functions, "sFunctions can't be empty");
        int count = functions.length;
        String[] names = new String[count];
        for (int i = 0; i < count; i++) {
            String methodName = LambdaUtils.extract(functions[i]).getImplMethodName();
            String propertyName = PropertyNamer.methodToProperty(methodName);
            names[i] = propertyName;
        }
        if (count == 1) {
            // one sFunction
            String name = names[0];
            SFunction<T, ?> function = functions[0];
            return o -> name + EQUAL + function.apply(o);
        }
        // two or more sFunction
        // sort by name first
        List<SimpleEntry<String, ? extends SFunction<T, ?>>> sorted = IntStream.range(0, count)
                .mapToObj(i -> new SimpleEntry<>(names[i], functions[i]))
                .sorted(Entry.comparingByKey())
                .collect(Collectors.toList());
        for (int i = 0; i < count; i++) {
            names[i] = sorted.get(i).getKey();
            functions[i] = sorted.get(i).getValue();
        }
        return o -> {
            StringJoiner stringJoiner = new StringJoiner(COLON);
            for (int i = 0; i < count; i++) {
                Function<T, ?> function = functions[i];
                Object value = function.apply(o);
                if (value != null) {
                    String propertyName = names[i];
                    stringJoiner.add(propertyName + EQUAL + value);
                }
            }
            return stringJoiner.toString();
        };
    }

    @SuppressWarnings("ConfusingArgumentToVarargsMethod")
    public static Function<Object[], Object> cacheKeyGenerator(Class<?> clazz, String methodName) {
        Method method = ReflectionUtils.findMethod(clazz, methodName, null);
        return cacheKeyGenerator(Objects.requireNonNull(method));
    }

    static final Map<Method, Function<Object[], Object>> METHOD_CACHE_KEY_GENERATOR_CACHE = new ConcurrentHashMap<>();

    /**
     * create a cache key generator
     *
     * @param method method
     * @return {@link Function}<{@link Object[]}, {@link Object}>
     * @see #cacheKeyGenerator(Class, String)
     */
    public static Function<Object[], Object> cacheKeyGenerator(Method method) {
        return METHOD_CACHE_KEY_GENERATOR_CACHE.computeIfAbsent(method, m -> {
            int count = m.getParameterCount();
            if (count == 0) {
                throw new IllegalStateException("method must has parameter");
            }
            Parameter[] parameters = m.getParameters();
            String[] names = new String[count];
            for (int i = 0; i < parameters.length; i++) {
                names[i] = ReflectUtil.getPrameterName(parameters[i]);
            }
            if (count == 1) {
                // one parameter
                String name = names[0];
                return params -> name + EQUAL + params[0];
            }
            // two or more parameter
            // sort by name first
            Integer[] indexSortedTmp = IntStream.range(0, count)
                    .mapToObj(i -> new SimpleEntry<>(names[i], i))
                    .sorted(Entry.comparingByKey())
                    .peek(entry -> names[entry.getValue()] = entry.getKey())
                    .map(Entry::getValue)
                    .toArray(Integer[]::new);
            int[] indexSorted = new int[count];
            for (int i = 0; i < count; i++) {
                indexSorted[i] = indexSortedTmp[i];
            }
            return params -> {
                StringJoiner stringJoiner = new StringJoiner(COLON);
                for (int i = 0; i < count; i++) {
                    int ind = indexSorted[i];
                    Object value = params[ind];
                    if (value != null) {
                        String name = names[ind];
                        stringJoiner.add(name + EQUAL + value);
                    }
                }
                return stringJoiner.toString();
            };
        });
    }
}
