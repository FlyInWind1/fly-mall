package fly.spring.security.util

import fly.spring.common.exception.GeneralException
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import mu.KotlinLogging
import org.apache.commons.lang3.StringUtils
import org.apache.commons.lang3.time.DateUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.util.*
import javax.crypto.spec.SecretKeySpec

@Component
class JwtUtil {
    @Value("\${jwt.secret:secret}")
    private var secret: String? = "secretfdjsoajfidosafjesaojfoieanfdalfdjasljifjdljfidoajfildjsaajo"

    @Value("\${jwt.expiration:999}")
    private var expiration: Long = 999

    private val key = SecretKeySpec(secret!!.toByteArray(), SignatureAlgorithm.HS256.jcaName)

    companion object {
        val log = KotlinLogging.logger { }
    }

    /**
     * generate jws
     */
    fun generate(claims: Map<String, Any>): String {
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(Date(System.currentTimeMillis() + expiration * 1000))
                .signWith(key)
                .compact()
    }

    /**
     * get body from jws
     */
    fun getClaims(jws: String): Claims {
        try {
            return Jwts
                    .parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(jws)
                    .body
        } catch (e: Exception) {
            log.info("Jwt auth failed:{}", jws)
            throw GeneralException("auth failed")
        }
    }

    fun getSubject(jws: String): String {
        return getClaims(jws).subject
    }

    fun validate(jws: String, userDetails: UserDetails): Boolean {
        val claims = getClaims(jws)
        return claims.subject == userDetails.username
                && !isExpired(claims)
    }

    fun isExpired(claims: Claims): Boolean {
        return Date().after(claims.expiration)
    }

    fun isExpired(jws: String): Boolean {
        return Date().after(getExpiredDate(jws))
    }

    fun getExpiredDate(jws: String): Date {
        return getClaims(jws).expiration
    }

    fun generate(userDetails: UserDetails): String {
        return this.generate(mapOf(
                "sub" to userDetails.username,
                "created" to Date()
        ))
    }

    fun refreshHead(oldJws: String): String {
        if (StringUtils.isNotEmpty(oldJws)) return StringUtils.EMPTY

        val claims = getClaims(oldJws)
        //can't refresh if expired
        if (isExpired(claims)) return StringUtils.EMPTY
        //return old if refreshed with in 30 minutes
        if (refreshedWithIn(claims, 30 * 60))
            return oldJws

        claims["created"] = Date()
        return generate(claims)
    }

    fun refreshedWithIn(claims: Claims, seconds: Int): Boolean {
        val created = claims.get("created", Date::class.java)
        val refreshDate = Date()

        if (created == null || created.after(refreshDate))
            return false

        return refreshDate.before(DateUtils.addSeconds(created, seconds))
    }
}