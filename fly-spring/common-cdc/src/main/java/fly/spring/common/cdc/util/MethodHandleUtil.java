package fly.spring.common.cdc.util;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.BeanUtils;

import java.beans.PropertyDescriptor;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.util.function.Function;

public class MethodHandleUtil {

    @SneakyThrows(IllegalAccessException.class)
    public static MethodHandle getterMethodHandle(PropertyDescriptor propertyDescriptor) {
        if (propertyDescriptor == null) {
            return null;
        }
        return MethodHandles.lookup().unreflect(propertyDescriptor.getReadMethod());
    }

    public static <I, O> Function<I, O> getterFunction(Class<?> clazz, String property) {
        return getterFunction(BeanUtils.getPropertyDescriptor(clazz, property));
    }

    public static <I, O> Function<I, O> getterFunction(PropertyDescriptor propertyDescriptor) {
        MethodHandle methodHandle = getterMethodHandle(propertyDescriptor);
        if (methodHandle == null) {
            return null;
        }
        return new GetterFunction<>(methodHandle);
    }

    @AllArgsConstructor
    public static class GetterFunction<I, O> implements Function<I, O> {
        protected final MethodHandle methodHandle;

        @Override
        @SneakyThrows
        @SuppressWarnings("AlibabaSneakyThrowsWithoutExceptionType")
        public O apply(I i) {
            //noinspection unchecked
            return (O) methodHandle.invoke(i);
        }
    }

}
