package fly.spring.common.core.util;

import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.core.toolkit.LambdaUtils;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
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

public class CacheKeyGeneratorUtil {
    public static final String COLON = ":";
    public static final String EQUAL = "=";

    /**
     * 创建缓存key生成器
     *
     * <pre>{@code
     * Function<Domain, Object> cacheKeyGenerator = CacheKeyGeneratorUtil
     *      .cacheKeyGenerator(Domain::getProp2, Domain::getProp1);
     * Domain domain = new Domain();
     * domain.setProp1("p1");
     * domain.setProp2("p2");
     * Object cacheKey = cacheKeyGenerator.apply(domain);
     * // cacheKey = "prop1=p1:prop2=p2"
     * }</pre>
     *
     * @param functions sFunctions
     * @return 缓存key生成器
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
                String propertyName = names[i];
                stringJoiner.add(propertyName + EQUAL + value);
            }
            return stringJoiner.toString();
        };
    }

    /**
     * 创建缓存key生成器
     *
     * <pre>{@code
     * static final Function<Object[], Object> GET_DOMAIN_KEY_GENERATOR
     *      = cacheKeyGenerator(XXX.class, "getDomainKey");
     *
     * public static Object getDomainKey(String prop2, String prop1) {
     *     // new Object[]{prop2, prop1} 顺序需要和参数循序相同
     *     return GET_DOMAIN_KEY_GENERATOR.apply(new Object[]{prop2, prop1});
     * }
     *
     * public static void main(String[] args) {
     *     Object cacheKey = getDomainKey("p2", "p1");
     *     // cacheKey = "prop1=p1:prop2=p2"
     * }
     * }</pre>
     *
     * @param clazz      方法所在类
     * @param methodName 方法名
     * @return 缓存key生成器
     */
    public static Function<Object[], Object> cacheKeyGenerator(Class<?> clazz, String methodName, Class<?>... paramTypes) {
        if (paramTypes.length == 0) {
            // 无视参数类型搜索方法
            paramTypes = null;
        }
        Method method = ReflectionUtils.findMethod(clazz, methodName, paramTypes);
        return cacheKeyGenerator(Objects.requireNonNull(method));
    }

    static final Map<Method, Function<Object[], Object>> METHOD_CACHE_KEY_GENERATOR_CACHE = new ConcurrentHashMap<>();

    /**
     * 创建缓存key生成器
     *
     * @param method method
     * @return 缓存key生成器
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
                names[i] = ReflectUtil.resolveParameterName(parameters[i]);
            }
            if (count == 1) {
                // one parameter
                String name = names[0];
                return params -> name + EQUAL + params[0];
            }
            // two or more parameter
            // sort by name first
            List<Integer> indexSortedTmp = IntStream.range(0, count)
                    .mapToObj(i -> new SimpleEntry<>(names[i], i))
                    .sorted(Entry.comparingByKey())
                    .map(Entry::getValue)
                    .collect(Collectors.toList());
            int[] indexSorted = new int[count];
            String[] nameSorted = new String[count];
            for (int i = 0; i < count; i++) {
                indexSorted[i] = indexSortedTmp.get(i);
                nameSorted[i] = names[indexSortedTmp.get(i)];
            }
            return params -> {
                StringJoiner stringJoiner = new StringJoiner(COLON);
                for (int i = 0; i < count; i++) {
                    Object value = params[indexSorted[i]];
                    String name = nameSorted[i];
                    stringJoiner.add(name + EQUAL + value);
                }
                return stringJoiner.toString();
            };
        });
    }
}
