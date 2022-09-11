package fly.spring.common.exception

open class GeneralException : RuntimeException {
    var respMessage: String = ""

    constructor()

    constructor(respMessage: String) {
        this.respMessage = respMessage
    }

    constructor(respMessage: String, message: String) : super(message) {
        this.respMessage = respMessage
    }
}