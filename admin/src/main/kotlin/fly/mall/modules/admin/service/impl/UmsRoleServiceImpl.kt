package fly.mall.modules.admin.service.impl

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import fly.mall.modules.admin.dao.UmsRoleDao
import fly.mall.modules.admin.pojo.po.UmsRolePo
import fly.mall.modules.admin.service.UmsRoleService
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

@Service
class UmsRoleServiceImpl : ServiceImpl<UmsRoleDao, UmsRolePo>(), UmsRoleService {
    @Cacheable()
    override fun listByAdminId(adminId: Int): List<UmsRolePo> {
        return baseMapper.listByAdminId(adminId)
    }
}