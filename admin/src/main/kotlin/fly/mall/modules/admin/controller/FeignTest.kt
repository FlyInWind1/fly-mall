package fly.mall.modules.admin.controller

import org.springframework.web.bind.annotation.GetMapping
import reactivefeign.spring.config.ReactiveFeignClient
import reactor.core.publisher.Mono

@ReactiveFeignClient(name = "feignTest", url = "https://www.baidu.com")
interface FeignTest {
    @GetMapping
    fun get(): Mono<String>
}
