package fly.mall.modules.admin.service.impl

import org.junit.jupiter.api.Test
import reactor.core.publisher.Mono

class SimpleTest {

    @Test
    fun test1() {
        val defer = Mono.defer { Mono.just("a") }
            .doOnNext { println(it) }
        defer.subscribe()
        Thread.sleep(1000)
    }
}
