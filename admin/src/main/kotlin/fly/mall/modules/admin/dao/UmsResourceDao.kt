package fly.mall.modules.admin.dao

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import fly.mall.modules.admin.pojo.po.UmsResourcePo
import org.apache.ibatis.annotations.Mapper

@Mapper
interface UmsResourceDao : BaseMapper<UmsResourcePo> {
    fun listByAdminId(adminId: Int): List<UmsResourcePo>
}