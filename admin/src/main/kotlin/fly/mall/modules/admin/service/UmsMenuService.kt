package fly.mall.modules.admin.service

import com.baomidou.mybatisplus.extension.service.IService
import fly.mall.modules.admin.pojo.po.UmsMenuPo

interface UmsMenuService : IService<UmsMenuPo> {
    fun listByAdminId(adminId: Int): List<UmsMenuPo>
}