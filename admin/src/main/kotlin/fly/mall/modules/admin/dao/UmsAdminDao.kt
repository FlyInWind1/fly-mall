package fly.mall.modules.admin.dao

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import fly.mall.modules.admin.pojo.po.UmsAdminPo
import org.apache.ibatis.annotations.Mapper

@Mapper
interface UmsAdminDao : BaseMapper<UmsAdminPo> {
}