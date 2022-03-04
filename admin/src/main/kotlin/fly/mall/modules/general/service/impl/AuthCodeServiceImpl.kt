package fly.mall.modules.general.service.impl

import fly.mall.modules.general.constant.Constant.REDIS_AUTH_CODE_KEY
import fly.mall.modules.general.service.AuthCodeService
import fly.spring.common.service.RedisService
import org.springframework.stereotype.Service
import kotlin.random.Random

@Service
class AuthCodeServiceImpl(
        val redisService: RedisService
) : AuthCodeService {

    override fun generateAuthCode(telephone: String): String {
        val builder = StringBuilder(6)
        for (i in 1..6) {
            builder.append(Random.nextInt(10))
        }
        val result = builder.toString()
        redisService.set("$REDIS_AUTH_CODE_KEY:$telephone", result)
        return ""
    }

    override fun verifyAuthCode(telephone: String, authCode: String): Boolean {
        val key = "$REDIS_AUTH_CODE_KEY:$telephone"
        val savedAuthCode: String? = redisService.get(key) ?: return false
        val result = savedAuthCode == authCode
        if (result) {
            redisService.del(key)
        }
        return result
    }
}