package fly.mall.modules.admin.service

import com.baomidou.mybatisplus.extension.service.IService
import fly.mall.modules.admin.pojo.po.UmsRolePo

interface UmsRoleService : IService<UmsRolePo> {
    fun listByAdminId(adminId: Int): List<UmsRolePo>
}