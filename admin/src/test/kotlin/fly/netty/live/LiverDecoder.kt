package fly.netty.live

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.ReplayingDecoder
import mu.KotlinLogging

class LiverDecoder : ReplayingDecoder<LiverDecoder.LiveState>(LiveState.TYPE) {

    companion object {
        val logger = KotlinLogging.logger { }
    }

    enum class LiveState {
        TYPE,
        LENGTH,
        CONTENT
    }

    private var message: LiveMessage? = null

    override fun decode(ctx: ChannelHandlerContext?, `in`: ByteBuf?, out: MutableList<Any>?) {
        val state = state()
        logger.debug("state:$state message:$message")
        when (state) {
            LiveState.TYPE -> {
                message = LiveMessage()
                val type = `in`!!.readByte()
                message!!.type = type
                checkpoint(LiveState.LENGTH)
            }
            LiveState.LENGTH -> {
                val length = `in`!!.readInt()
                message!!.length = length
                if (length > 0) {
                    checkpoint(LiveState.CONTENT)
                } else {
                    out!!.add(message!!)
                    checkpoint(LiveState.TYPE)
                }
            }
            LiveState.CONTENT -> {
                val bytes = ByteArray(message!!.length!!)
                `in`!!.readBytes(bytes)
                val content = String(bytes)
                message!!.content = content
                out!!.add(message!!)
                checkpoint(LiveState.TYPE)
            }
            else -> throw IllegalStateException()
        }
        logger.debug("end state:$state list:$out"); }
}
