package fly.spring.common.cdc.util;

import org.apache.commons.lang3.reflect.MethodUtils;

import java.lang.reflect.Method;

public class ClassUtil {
    public static boolean isMethodEquals(Class<?> firstClass, Class<?> secondClass, String methodName, Class<?>... parameterType) {
        Method firstMethod = MethodUtils.getMatchingMethod(firstClass, methodName, parameterType);
        Method secondMethod = MethodUtils.getMatchingMethod(secondClass, methodName, parameterType);
        return firstMethod.equals(secondMethod);
    }

    public static boolean isMethodNotEquals(Class<?> firstClass, Class<?> secondClass, String methodName, Class<?>... parameterType) {
        return !isMethodEquals(firstClass, secondClass, methodName, parameterType);
    }

}
