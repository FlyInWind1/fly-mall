package fly.mall.modules.admin.controller

import cn.hutool.extra.spring.SpringUtil
import fly.mall.modules.admin.pojo.dto.UmsAdminDto
import fly.mall.modules.admin.pojo.po.UmsAdminPo
import fly.mall.modules.admin.pojo.vo.LoginInfoVo
import fly.mall.modules.admin.service.UmsAdminService
import fly.mall.modules.admin.service.UmsMenuService
import fly.mall.modules.admin.service.UmsRoleService
import fly.spring.common.kotlin.extension.rok
import fly.spring.common.pojo.R
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono
import java.nio.file.attribute.GroupPrincipal
import java.util.stream.Collectors
import javax.security.auth.login.CredentialExpiredException

@RestController
@RequestMapping("admin")
class UmsAdminController(
    private val adminService: UmsAdminService,
    private val menuService: UmsMenuService,
    private val roleService: UmsRoleService
) {

    @PostMapping("register")
    fun register(@Validated userDto: UmsAdminDto): Mono<R<UmsAdminPo?>> {
        return adminService.register(userDto).rok()
    }

    @PostMapping("login")
    fun login(@RequestBody @Validated userDto: UmsAdminDto): Mono<R<Map<String, String>>> {
        val token = adminService.login(userDto.username, userDto.password)
        return mapOf("token" to token).rok()
    }

    @GetMapping("info")
    fun info(principal: GroupPrincipal?): Mono<R<LoginInfoVo>> {
        principal ?: throw CredentialExpiredException()
        val username = principal.name
        val admin = adminService.getByUsername(username)
        val adminId = admin?.id!!
        val vo = LoginInfoVo(username)
        vo.menus = menuService.listByAdminId(adminId)
        vo.icon = admin.icon
        vo.roles = roleService.listByAdminId(adminId).stream().map { it.name!! }.collect(Collectors.toList())
        return vo.rok()
    }

    @GetMapping("feignTest")
    fun feignTest(): Mono<String> {
        val feignTest = SpringUtil.getBean(FeignTest::class.java)
        return feignTest.get()
    }
}
