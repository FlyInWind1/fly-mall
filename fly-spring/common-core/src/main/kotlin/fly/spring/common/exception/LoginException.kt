package fly.spring.common.exception

class LoginException(message: String) : GeneralException(message) {
    var returnCode: Short? = null
}