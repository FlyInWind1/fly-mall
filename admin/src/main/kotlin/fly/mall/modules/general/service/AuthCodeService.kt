package fly.mall.modules.general.service

interface AuthCodeService {

    /**
     * generate auth code
     */
    fun generateAuthCode(telephone: String): String

    /**
     * verify a verifyCode
     */
    fun verifyAuthCode(telephone: String, authCode: String): Boolean
}