package fly.netty.live

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToByteEncoder
import io.netty.util.internal.StringUtil

class LiveEncoder : MessageToByteEncoder<LiveMessage>() {
    override fun encode(ctx: ChannelHandlerContext?, msg: LiveMessage?, out: ByteBuf?) {
        out!!.writeByte(msg!!.type!!.toInt())
        out.writeInt(msg.length!!)
        if (!StringUtil.isNullOrEmpty(msg.content)) {
            out.writeBytes(msg.content!!.toByteArray())
        }
    }
}
