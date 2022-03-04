package fly.mall.modules.admin.dao

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import fly.mall.modules.admin.pojo.po.UmsRolePo
import org.apache.ibatis.annotations.Mapper

@Mapper
interface UmsRoleDao : BaseMapper<UmsRolePo> {
    fun listByAdminId(adminId: Int): List<UmsRolePo>
}