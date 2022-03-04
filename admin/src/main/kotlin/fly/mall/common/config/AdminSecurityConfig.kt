package fly.mall.common.config

import fly.mall.modules.admin.service.UmsAdminService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.core.userdetails.UserDetailsService

@Configuration
class AdminSecurityConfig {
    @Bean
    fun userDetailService(umsAdminService: UmsAdminService): UserDetailsService {
        return UserDetailsService { umsAdminService.loalUserDetailByUsername(it) }
    }
}