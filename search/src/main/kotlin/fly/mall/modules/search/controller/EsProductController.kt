package fly.mall.modules.search.controller

import fly.mall.modules.search.service.ProductService
import fly.spring.common.kotlin.extension.rok
import fly.spring.common.pojo.R
import fly.spring.common.util.MonoR
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

@RestController
@RequestMapping("search")
class EsProductController(
        private val productService: ProductService
) {

    @GetMapping("importAll")
    fun importAll(): Mono<R<Int>> {
        return productService.importAll().mapOk()
    }

    @DeleteMapping("delete/{id}")
    fun deleteByIds(@PathVariable("id") id: Long): Mono<R<String?>> {
//        productService.deleteById(id)
        return MonoR.ok()
    }
}