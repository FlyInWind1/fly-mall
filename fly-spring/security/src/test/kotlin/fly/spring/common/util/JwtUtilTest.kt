package fly.spring.common.util

import com.fasterxml.jackson.databind.ObjectMapper
import fly.spring.security.util.JwtUtil
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.junit.jupiter.api.Test

class JwtUtilTest {
    @Test
    fun generateKeySecret() {
        val key = Keys.secretKeyFor(SignatureAlgorithm.HS256)
        val byteArray = key.encoded
        val result = ObjectMapper().writeValueAsString(byteArray)
//        val charList = byteArray.asList().stream().map { it.toChar() }.collect(Collectors.toList())
//        println(Arrays.toString(byteArray))
//        ByteArray()
//        println(charList.)
        println(result)
    }

    @Test
    fun generate() {
        println(JwtUtil().generate(mapOf()))
    }

    @Test
    fun verity() {
        val token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJmbHlpbndpbmQifQ.BlMrb5v3ZLDKB0AxV5QYs8gX1gK-50c_x3i3MO9Zv2zEFQA1GYH07aL0W6nTxbbJkkbseghR4zEDL83ZSrlSWw"
        val jwtUtil = JwtUtil()
//        val token =jwtUtil.generateToken(mapOf())
        val claims = jwtUtil.getClaims(token)
        print(claims.subject)
    }
}