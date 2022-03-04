package fly.mall.modules.admin.service.impl

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import fly.mall.modules.admin.dao.UmsMenuDao
import fly.mall.modules.admin.pojo.po.UmsMenuPo
import fly.mall.modules.admin.service.UmsMenuService
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

@Service
class UmsMenuServiceImpl : ServiceImpl<UmsMenuDao, UmsMenuPo>(), UmsMenuService {
    @Cacheable()
    override fun listByAdminId(adminId: Int): List<UmsMenuPo> {
        return baseMapper.listByAdminId(adminId)
    }
}