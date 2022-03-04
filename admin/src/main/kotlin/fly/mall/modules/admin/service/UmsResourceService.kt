package fly.mall.modules.admin.service

import com.baomidou.mybatisplus.extension.service.IService
import fly.mall.modules.admin.pojo.po.UmsResourcePo

interface UmsResourceService : IService<UmsResourcePo> {
    fun listByAdminId(adminId: Int): List<UmsResourcePo>
}