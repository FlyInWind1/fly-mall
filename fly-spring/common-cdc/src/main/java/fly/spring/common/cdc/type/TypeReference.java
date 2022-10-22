package fly.spring.common.cdc.type;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import lombok.Getter;

/**
 * 基于jackson的 {@link TypeFactory}，读取泛型参数类型 {@link T}
 *
 * @author FlyInWind
 * @since 2022/08/09
 */
public abstract class TypeReference<T> {
    /** 泛型参数类型 */
    @Getter
    protected final JavaType parameterType;

    protected TypeReference() {
        TypeFactory typeFactory = TypeFactory.defaultInstance();
        JavaType currentType = typeFactory.constructType(getClass());
        JavaType[] typeParameters = typeFactory.findTypeParameters(currentType, TypeReference.class);
        parameterType = typeParameters[0];
    }

    protected Class<T> getParameterClass() {
        //noinspection unchecked
        return (Class<T>) parameterType.getRawClass();
    }
}
