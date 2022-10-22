package fly.spring.common.core.util;

import org.apache.ibatis.annotations.Param;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class ReflectUtil {

    static final Function<Annotation, String>[] ANNOTATION_PARAMETER_RESOLVERS;

    static {
        List<Function<Annotation, String>> resolvers = new ArrayList<>();
        try {
            Class.forName(Param.class.getName());
            resolvers.add(annotation -> {
                if (annotation instanceof Param) {
                    return ((Param) annotation).value();
                }
                return null;
            });
        } catch (ClassNotFoundException e) {
            // ignore
        }
        //noinspection unchecked
        ANNOTATION_PARAMETER_RESOLVERS = resolvers.toArray(new Function[0]);
    }

    /**
     * 寻找 {@code @Param("xxx")} 注解，返回其 value，如果没有注解，返回参数名
     *
     * @param parameter 参数
     * @return 参数名
     */
    public static String resolveParameterName(Parameter parameter) {
        Objects.requireNonNull(parameter);
        for (Annotation annotation : parameter.getAnnotations()) {
            for (Function<Annotation, String> parameterResolver : ANNOTATION_PARAMETER_RESOLVERS) {
                String name = parameterResolver.apply(annotation);
                if (name != null) {
                    return name;
                }
            }
        }
        return parameter.getName();
    }
}
