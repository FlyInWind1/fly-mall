package fly.mall.modules.admin.service

import com.baomidou.mybatisplus.extension.service.IService
import fly.mall.common.config.AdminUserDetail
import fly.mall.modules.admin.pojo.dto.UmsAdminDto
import fly.mall.modules.admin.pojo.po.UmsAdminPo

interface UmsAdminService : IService<UmsAdminPo> {
    fun register(userDto: UmsAdminDto): UmsAdminPo?

    fun getByUsername(username: String): UmsAdminPo?

    fun loalUserDetailByUsername(username: String): AdminUserDetail

    fun login(username: String, password: String): String
}