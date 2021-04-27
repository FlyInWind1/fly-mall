//package fly.spring.security.component
//
//import org.springframework.http.CacheControl
//import org.springframework.http.HttpHeaders
//import org.springframework.http.MediaType
//import org.springframework.security.core.AuthenticationException
//import org.springframework.security.web.server.ServerAuthenticationEntryPoint
//import org.springframework.web.server.ServerWebExchange
//import reactor.core.publisher.Mono
//
//class RestAuthenticationEntryPoint : ServerAuthenticationEntryPoint {
//
//    override fun commence(exchange: ServerWebExchange?, e: AuthenticationException?): Mono<Void> {
//        val response = exchange!!.response
//        val responseHeaders = response.headers
//        responseHeaders[HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN] = "*"
//        responseHeaders[HttpHeaders.CACHE_CONTROL] = CacheControl.noCache().headerValue
//        responseHeaders.contentType = MediaType.APPLICATION_JSON
//        response.writeWith{
////            it.
//        }
//    }
//
//}