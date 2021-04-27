package fly.mall.modules.admin.service.impl

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import fly.mall.modules.admin.dao.UmsResourceDao
import fly.mall.modules.admin.pojo.po.UmsResourcePo
import fly.mall.modules.admin.service.UmsResourceService
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

@Service
class UmsResourceServiceImpl : ServiceImpl<UmsResourceDao, UmsResourcePo>(), UmsResourceService {
    @Cacheable()
    override fun listByAdminId(adminId: Int): List<UmsResourcePo> {
        return baseMapper.listByAdminId(adminId)
    }
}