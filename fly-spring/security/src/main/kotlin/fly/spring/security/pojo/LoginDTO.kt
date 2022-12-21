package fly.spring.security.pojo

class LoginDTO : BaseUser() {
    /**
     * 唯一标识
     */
    private val uuid: String? = null

    /** 验证码  */
    private val code: String? = null
}
