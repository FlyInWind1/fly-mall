package fly.netty.live

import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import mu.KotlinLogging
import java.util.concurrent.TimeUnit

class LiveHandler : SimpleChannelInboundHandler<LiveMessage>() {
    companion object {
        private val channelCache = HashMap<Int, LiveChannelCache>()
    }

    private val log = KotlinLogging.logger { }

    override fun channelRead0(ctx: ChannelHandlerContext?, msg: LiveMessage?) {
        val channel = ctx!!.channel()
        val hashCode = channel.hashCode()
        log.debug("channel hashCode:" + hashCode + " msg:" + msg + " cache:" + channelCache.size)

        if (!channelCache.containsKey(hashCode)) {
            log.debug("channelCache.containsKey(hashCode), put key:$hashCode")
            channel.closeFuture().addListener {
                log.debug { "channel close, remove key:$hashCode" }
                channelCache.remove(hashCode)
            }
            val scheduledFuture = ctx.executor().schedule({
                log.debug { "schedule runs, close channel:$hashCode" }
                channel.close()
            }, 10, TimeUnit.SECONDS)
            channelCache[hashCode] = LiveChannelCache(channel, scheduledFuture)
        }

        when (msg!!.type) {
            LiveMessage.TYPE_HEART -> {
                val cache = channelCache[hashCode]
                val scheduledFuture = ctx.executor().schedule({
                    channel.closeFuture()
                }, 5, TimeUnit.SECONDS)
                cache!!.scheduledFuture.cancel(true)
                cache.scheduledFuture = scheduledFuture
                ctx.channel().writeAndFlush(msg)
            }
            LiveMessage.TYPE_MESSAGE -> {
                channelCache.entries.stream().forEach { entry ->
                    val otherChannel = entry.value.channel
                    otherChannel.writeAndFlush(msg)
                }
            }
        }
    }

    override fun channelReadComplete(ctx: ChannelHandlerContext?) {
        log.debug("channelReadComplete")
        super.channelReadComplete(ctx)
    }

    override fun exceptionCaught(ctx: ChannelHandlerContext?, cause: Throwable?) {
        log.debug("exceptionCaught", cause)
        ctx!!.close()
    }
}
