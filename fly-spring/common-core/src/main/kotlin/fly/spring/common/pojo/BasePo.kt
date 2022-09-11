package fly.spring.common.pojo

import java.time.LocalDateTime

open class BasePo {
    var id: Int? = null

    var createTime: LocalDateTime? = null

    var updateTime: LocalDateTime? = null

    var delFlag: Boolean? = null
}