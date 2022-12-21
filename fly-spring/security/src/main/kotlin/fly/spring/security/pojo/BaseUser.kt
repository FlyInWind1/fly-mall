package fly.spring.security.pojo

import fly.spring.security.inferfaces.IUser
import org.springframework.security.core.GrantedAuthority
import java.util.*

open class BaseUser : IUser {
    override var id: Int? = null

    val username: String? = null

    override var password: String? = null

    override fun getUsername(): String? {
        return username
    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return Collections.emptyList()
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }

}
