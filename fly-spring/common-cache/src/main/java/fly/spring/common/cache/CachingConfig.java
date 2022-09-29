package fly.spring.common.cache;

import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.interceptor.KeyGenerator;

/**
 * @author FlyInWind
 * @date 2021/12/26
 */
@AllArgsConstructor
public class CachingConfig extends CachingConfigurerSupport {
    private final ParameterKeyGenerator keyGenerator;

    @Override
    public KeyGenerator keyGenerator() {
        return keyGenerator;
    }
}
