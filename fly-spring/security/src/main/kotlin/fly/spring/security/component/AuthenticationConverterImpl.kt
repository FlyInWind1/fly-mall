package fly.spring.security.component

import com.fasterxml.jackson.databind.ObjectMapper
import fly.spring.security.pojo.LoginDTO
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.core.io.buffer.DataBufferUtils
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
class JsonBodyAuthenticationConverter(
    private val objectMapper: ObjectMapper
) : ServerAuthenticationConverter {
    override fun convert(exchange: ServerWebExchange): Mono<Authentication> {
        return DataBufferUtils.join(exchange.request.body)
            .map { dataBuffer ->
                this.convert(
                    dataBuffer
                )
            }
    }

    protected fun convert(dataBuffer: DataBuffer): Authentication {
        val dto = objectMapper.readValue(dataBuffer.asInputStream(), LoginDTO::class.java)
        val username = dto.username
        val password = dto.password
        val token = UsernamePasswordAuthenticationToken(username, password)
        token.details = dto
        return token
    }
}
