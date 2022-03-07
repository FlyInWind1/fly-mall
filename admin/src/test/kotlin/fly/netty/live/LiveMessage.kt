package fly.netty.live

class LiveMessage {
    companion object {
        const val TYPE_HEART: Byte = 1
        const val TYPE_MESSAGE: Byte = 2
    }

    var type: Byte? = null

    var length: Int? = null

    var content: String? = null
}
