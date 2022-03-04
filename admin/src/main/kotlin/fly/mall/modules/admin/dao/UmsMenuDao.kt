package fly.mall.modules.admin.dao

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import fly.mall.modules.admin.pojo.po.UmsMenuPo
import org.apache.ibatis.annotations.Mapper

@Mapper
interface UmsMenuDao : BaseMapper<UmsMenuPo> {
    fun listByAdminId(adminId: Int): List<UmsMenuPo>
}