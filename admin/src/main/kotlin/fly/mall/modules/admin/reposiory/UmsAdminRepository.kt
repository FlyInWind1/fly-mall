package fly.mall.modules.admin.reposiory;

import fly.mall.modules.admin.pojo.po.UmsAdminPo
import org.springframework.data.r2dbc.repository.R2dbcRepository

interface UmsAdminRepository : R2dbcRepository<UmsAdminPo, Int> {

    fun findByUsername(username: String)

}
