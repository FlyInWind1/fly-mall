package fly.mall.common.i18n

import org.springframework.context.support.ReloadableResourceBundleMessageSource
import java.util.*

object AdminI18n {
    private val messageSource = ReloadableResourceBundleMessageSource()

    init {
        messageSource.setBasename("classpath:admin")
    }

    fun getString(key: String, vararg args: Any): String {
        return messageSource.getMessage(key, args, Locale.US)
    }

    fun getString(key: String): String {
        return messageSource.getMessage(key, null, Locale.US)
    }

    fun getString(key: String, vararg args: Any, locale: Locale): String {
        return messageSource.getMessage(key, args, locale)
    }

    fun loginException(message: String): String? {
        return getString("login_exception", message)
    }

    val userHasBeanDisabled: String
        get() = getString("user_has_bean_disabled")

    fun usernameHasBeRegistered(username: String): String {
        return getString("username_has_been_registered", username)
    }

    val usernameOrPasswordIncorrect: String
        get() = getString("username_or_password_incorrect")

}