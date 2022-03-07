package fly.netty.live

import fly.netty.live.LiveServer.Companion.logger
import fly.netty.live.LiveServer.Companion.port
import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.ChannelInitializer
import io.netty.channel.ChannelOption
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel
import mu.KotlinLogging
import reactor.netty.tcp.TcpClient

class LiveServer(private val port: Int) {
    companion object {
        val logger = KotlinLogging.logger { }
        const val port = 8080
    }

    fun start() {
        val b = ServerBootstrap()
        val group = NioEventLoopGroup()
        b.group(group)
            .channel(NioServerSocketChannel::class.java)
            .childHandler(object : ChannelInitializer<SocketChannel>() {
                override fun initChannel(ch: SocketChannel) {
                    logger.debug { "initChannel ch:$ch" }
                    ch.pipeline()
                        .addLast("decoder", LiverDecoder())
                        .addLast("encoder", LiveEncoder())
                        .addLast("handler", LiveHandler())
                }
            })
            .option(ChannelOption.SO_BACKLOG, 128)
            .childOption(ChannelOption.SO_KEEPALIVE, true)
        b.bind(port).sync()
        TcpClient.create()
            .doOnConnect { }
    }
}

fun main(args: Array<String>) {
//    if (args.size != 1) {
//        logger.warn("Usage: " + LiveServer::class.simpleName + " <port>")
//        return
//    }
    logger.debug("start server with port:$port")
    LiveServer(port).start()
}
