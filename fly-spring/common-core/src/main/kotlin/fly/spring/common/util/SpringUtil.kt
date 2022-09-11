package fly.spring.common.util

import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext

/**
 * some tools for spring boot
 * @link https://github.com/looly/hutool/blob/v5-master/hutool-extra/src/main/java/cn/hutool/extra/spring/SpringUtil.java
 */
class SpringUtil : ApplicationContextInitializer<ConfigurableApplicationContext> {
    companion object {
        lateinit var context: ConfigurableApplicationContext
    }

    override fun initialize(applicationContext: ConfigurableApplicationContext) {
        context = applicationContext
    }

    /**
     * get bean by bean name
     * @param T return type
     * @param name bean name
     * @return bean
     */
    @Suppress("unchecked_cast")
    fun <T> getBean(name: String): T {
        return context.getBean(name) as T
    }

    /**
     * get bean by bean type
     * @param T return type
     * @param clazz bean type
     * @return bean
     */
    fun <T> getBean(clazz: Class<T>): T {
        return context.getBean(clazz)
    }

    /**
     * get bean by bean type
     * @param T return type
     * @param name bean name
     * @param clazz bean type
     * @return bean
     */
    fun <T> getBean(name: String, clazz: Class<T>): T {
        return context.getBean(name, clazz)
    }

    /**
     * get bean by bean type
     * @param T return type
     * @param clazz bean type
     * @return bean
     */
    fun <T> getBeansOfType(clazz: Class<T>): Map<String, T> {
        return context.getBeansOfType(clazz)
    }

    /**
     * get value of a key from configuration
     * @param key config key
     * @return value
     */
    fun getProperty(key: String): String? {
        return context.environment.getProperty(key)
    }
}