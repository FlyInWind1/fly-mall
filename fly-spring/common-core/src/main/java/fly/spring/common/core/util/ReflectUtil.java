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

    public static String getPrameterName(Parameter parameter) {
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
