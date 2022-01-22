package fly.mall.modules.admin.service.impl

import org.junit.jupiter.api.Test
import org.springframework.util.FileSystemUtils
import reactor.core.publisher.Mono
import java.nio.file.Path
import java.util.function.Supplier

class SimpleTest {
    @Test
    fun copy() {
        FileSystemUtils.copyRecursively(Path.of("/mnt/d/Pictures/微信图片_20201004132722.jpg"), Path.of("/tmp/a/a.jpg"))
    }

    @Test
    fun test1() {
        val defer = Mono.defer { Mono.just("a") }
            .doOnNext { println(it) }
        defer.subscribe()
        Thread.sleep(1000)
    }
}
