package fly.netty.live

import fly.netty.live.LiveServer
import kotlinx.coroutines.*
import mu.KotlinLogging
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket
import java.nio.ByteBuffer
import java.util.*

class LongConnTest {
    companion object {
        val logger = KotlinLogging.logger { }
    }

    private val host = "127.0.0.1"
    private val port = LiveServer.port

    @OptIn(DelicateCoroutinesApi::class)
    suspend fun testLongConn(): Unit {
        logger.debug("start")
        val socket = Socket()
        withContext(Dispatchers.IO) {
            socket.connect(InetSocketAddress(host, port))
        }
        val scanner = Scanner(System.`in`)
        GlobalScope.launch {
            while (true) {
                try {
                    val input = ByteArray(64)
                    val readByte = withContext(Dispatchers.IO) {
                        socket.getInputStream().read(input)
                    }
                    logger.debug("readByte $readByte")
                } catch (e: IOException) {
                    logger.warn("", e)
                }
            }
        }
        while (true) {
            val code = scanner.nextInt()
            logger.debug("input code: $code")
            if (code == 0) {
                break
            } else if (code == 1) {
                val byteBuffer = ByteBuffer.allocate(5)
                byteBuffer.put(1)
                byteBuffer.putInt(0)
                withContext(Dispatchers.IO) {
                    socket.getOutputStream().write(byteBuffer.array())
                }
                logger.debug("write heart finish!")
            } else if (code == 2) {
                val content = ("hello, I'm" + hashCode()).toByteArray()
                val byteBuffer = ByteBuffer.allocate(content.size + 5)
                byteBuffer.put(2)
                byteBuffer.putInt(content.size)
                byteBuffer.put(content)
                withContext(Dispatchers.IO) {
                    socket.getOutputStream().write(byteBuffer.array())
                }
                logger.debug("write content finish!")
            }
            withContext(Dispatchers.IO) {
                socket.close()
            }
        }
    }
}

// 因为Junit不支持用户输入,所以用main的方式来执行用例
suspend fun main() {
    LongConnTest().testLongConn()

}
