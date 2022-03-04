package fly.mall.modules.admin.service.impl

import fly.mall.common.config.AdminUserDetail
import fly.mall.common.i18n.AdminI18n
import fly.mall.modules.admin.pojo.dto.UmsAdminDto
import fly.mall.modules.admin.pojo.po.UmsAdminPo
import fly.mall.modules.admin.reposiory.UmsAdminRepository
import fly.mall.modules.admin.service.UmsAdminService
import fly.mall.modules.admin.service.UmsResourceService
import fly.spring.common.exception.GeneralException
import fly.spring.common.exception.LoginException
import fly.spring.common.exception.UnknownException
import fly.spring.security.util.JwtUtil
import org.apache.commons.collections4.CollectionUtils
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UmsAdminServiceImpl(
    private val passwordEncoder: PasswordEncoder,
    private val resourceService: UmsResourceService,
    private val umsAdminRepository: UmsAdminRepository,
    private val jwtUtil: JwtUtil
) : UmsAdminService {

    override fun register(dto: UmsAdminDto): UmsAdminPo {
        val username = dto.username
        umsAdminRepository.findByUsername(dto.username)
        val userList = ktQuery().eq(UmsAdminPo::username, username).list()
        if (CollectionUtils.isNotEmpty(userList)) {
            throw GeneralException(AdminI18n.usernameHasBeRegistered(username))
        }
        val userPo = UmsAdminPo.fromDto(dto)
        userPo.password = passwordEncoder.encode(dto.password)
        this.save(userPo)
        return userPo
    }

    override fun getByUsername(username: String): UmsAdminPo? {
        return ktQuery()
            .eq(UmsAdminPo::username, username)
            .one()
    }

    override fun loalUserDetailByUsername(username: String): AdminUserDetail {
        val user = getByUsername(username)
            ?: throw GeneralException(AdminI18n.usernameOrPasswordIncorrect)
        val resources = resourceService.listByAdminId(user.id!!)
        return AdminUserDetail(user, resources)
    }

    override fun login(username: String, password: String): String {
        try {
            val userDetail = loalUserDetailByUsername(username)
            if (!passwordEncoder.matches(password, userDetail.password)) {
                throw LoginException(AdminI18n.usernameOrPasswordIncorrect)
            }
            if (!userDetail.isEnabled) {
                throw LoginException(AdminI18n.userHasBeanDisabled)
            }
            val authentication = UsernamePasswordAuthenticationToken(userDetail, null, userDetail.authorities)
            SecurityContextHolder.getContext().authentication = authentication
            // TODO: 2020/11/17 登录记录
            return jwtUtil.generate(userDetail)
        } catch (e: AuthenticationException) {
            log.warn(AdminI18n.loginException(e.message ?: ""))
        }
        throw UnknownException()
    }
}
