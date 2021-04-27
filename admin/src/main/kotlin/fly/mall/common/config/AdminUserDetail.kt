package fly.mall.common.config

import fly.mall.modules.admin.pojo.po.UmsAdminPo
import fly.mall.modules.admin.pojo.po.UmsResourcePo
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class AdminUserDetail(
    val umsAdmin: UmsAdminPo,
    val resourcePos: List<UmsResourcePo>
) : UserDetails {
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return mutableListOf(SimpleGrantedAuthority("1:admin"))
    }

    override fun getPassword(): String? {
        return umsAdmin.password
    }

    override fun getUsername(): String? {
        return umsAdmin.username
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