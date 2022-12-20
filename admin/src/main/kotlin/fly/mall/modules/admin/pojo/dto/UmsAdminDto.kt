package fly.mall.modules.admin.pojo.dto

import jakarta.validation.constraints.NotBlank

class UmsAdminDto {

    @NotBlank(message = "username can't be null")
    lateinit var username: String

    @NotBlank(message = "password can't be null")
    lateinit var password: String

    var icon: String? = null

    var email: String? = null

    var nickName: String? = null

    var note: String? = null

}
