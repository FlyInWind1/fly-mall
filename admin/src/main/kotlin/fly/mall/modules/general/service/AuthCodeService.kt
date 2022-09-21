package fly.mall.modules.general.service

import fly.mall.modules.general.constant.Constant.REDIS_AUTH_CODE_KEY
import org.springframework.data.redis.core.ReactiveValueOperations
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import kotlin.random.Random

@Service
class AuthCodeService(
    val valueOperations: ReactiveValueOperations<String, Any>
) {

    fun generateAuthCode(telephone: String): Mono<Boolean> {
        val builder = StringBuilder(6)
        for (i in 1..6) {
            builder.append(Random.nextInt(10))
        }
        val result = builder.toString()
        return valueOperations.set("$REDIS_AUTH_CODE_KEY:$telephone", result)
    }

    fun verifyAuthCode(telephone: String, authCode: String): Mono<Boolean> {
        val key = "$REDIS_AUTH_CODE_KEY:$telephone"
        return valueOperations.get(key)
            .flatMap { savedAuthCode ->
                if (savedAuthCode == null) {
                    return@flatMap false.toMono()
                }
                val result = savedAuthCode == authCode
                if (result) {
                    return@flatMap valueOperations.delete(key)
                        .thenReturn(true)
                }
                return@flatMap false.toMono()
            }
    }
}
