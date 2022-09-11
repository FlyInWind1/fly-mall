package fly.spring.common.bean;

import fly.spring.common.cglib.CglibUtil;
import lombok.Data;
import org.springframework.cglib.proxy.*;

import java.lang.reflect.Method;

public class BeanUtil {

    public static <T> T copyNonSettable(T obj) {
        if (obj == null) {
            return null;
        }
        Enhancer enhancer = new Enhancer();
        Class<?> clazz = obj.getClass();
        enhancer.setSuperclass(clazz);
        enhancer.setCallbackFilter(CONVERT_NOT_SETTABLE_FILTER);
        ConvertNotSettableCallback callback = new ConvertNotSettableCallback();
        enhancer.setCallbacks(new Callback[]{NoOp.INSTANCE, callback});
        //noinspection unchecked
        T result = (T) enhancer.create();
        CglibUtil.copy(obj, result, clazz, clazz, null);
        callback.setSettable(false);
        return result;
    }

    static final String SET = "set";

    static final CallbackFilter CONVERT_NOT_SETTABLE_FILTER = method -> {
        if (method.getName().startsWith(SET)) {
            return 1;
        }
        return 0;
    };

    @Data
    static class ConvertNotSettableCallback implements MethodInterceptor {
        private boolean settable = true;

        @Override
        public Object intercept(Object o, Method method, Object[] params, MethodProxy methodProxy) throws Throwable {
            if (!settable) {
                throw new IllegalAccessError("缓存对象禁止 set");
            }
            return methodProxy.invokeSuper(o, params);
        }
    }
}
