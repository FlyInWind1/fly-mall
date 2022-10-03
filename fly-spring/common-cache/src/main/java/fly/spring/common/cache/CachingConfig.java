package fly.spring.common.cache;

import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;

/**
 * @author FlyInWind
 * @date 2021/12/26
 */
@EnableCaching
public class CachingConfig extends CachingConfigurerSupport {
    private final KeyGenerator keyGenerator = new ParameterKeyGenerator();

    @Override
    public KeyGenerator keyGenerator() {
        return keyGenerator;
    }
}
