package fly.spring.security.component

import com.fasterxml.jackson.databind.ObjectMapper
import fly.spring.common.pojo.R
import org.springframework.http.MediaType
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

class RestAccessDeniedHandler(
        private val objectMapper: ObjectMapper
) : ServerAccessDeniedHandler {

    override fun handle(exchange: ServerWebExchange?, denied: AccessDeniedException?): Mono<Void> {
        val response = exchange!!.response
        val headers = response.headers
        headers.set("Access-Control-Allow-Origin", "*")
        headers.set("Cache-Control", "no-cache")
        headers.contentType = MediaType.APPLICATION_JSON
        response.writeWith {
            it.onNext(
                    response.bufferFactory()
                            .wrap(objectMapper.writeValueAsString(
                                    R.fail("失败")).toByteArray())
            )
        }
        return Mono.empty()
    }

}