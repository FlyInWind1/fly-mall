package fly.mall.modules.admin.pojo.po

import fly.mall.modules.admin.pojo.dto.UmsAdminDto
import fly.spring.common.pojo.BasePo
import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers
import java.time.LocalDateTime

class UmsAdminPo : BasePo() {

    var username: String? = null

    var password: String? = null

    var salt: String? = null

    var status: Byte? = null

    var icon: String? = null

    var email: String? = null

    var nickName: String? = null

    var note: String? = null

    var loginTime: LocalDateTime? = null

    companion object {
        @Mapper
        interface StructureMapper {
            fun fromDto(dto: UmsAdminDto): UmsAdminPo
        }

        private var mapper = Mappers.getMapper(StructureMapper::class.java)

        fun fromDto(dto: UmsAdminDto): UmsAdminPo {
            return mapper.fromDto(dto)
        }
    }
}