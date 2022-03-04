package fly.mall.modules.general.controller

import fly.mall.modules.general.pojo.dto.PhotoNumberDto
import fly.mall.modules.general.service.AuthCodeService
import fly.spring.common.kotlin.extension.rok
import fly.spring.common.pojo.R
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

@RestController
@RequestMapping("authCode")
class AuthCodeController(
        val authCodeService: AuthCodeService
) {

    @GetMapping("getAuthCode")
    fun getAuthCode(@RequestBody dto: PhotoNumberDto): Mono<R<String>> {
        return authCodeService.generateAuthCode(dto.photoNumber).rok()
    }

    @PostMapping("verifyAuthCode")
    fun checkAutoCode(telephone: String, authCode: String): Mono<R<Boolean>> {
        return authCodeService.verifyAuthCode(telephone, authCode).rok()
    }
}