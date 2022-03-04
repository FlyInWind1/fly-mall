package fly.spring.security.component

import fly.spring.security.util.JwtUtil
import mu.KotlinLogging
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono

class JwtAuthenticationTokenFilter(
        private val jwtUtil: JwtUtil,
        private val userDetailsService: UserDetailsService,
        private val jwtProperties: JwtProperties,
) : WebFilter {

    companion object {
        private val log = KotlinLogging.logger { }
    }

    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        val request = exchange.request
        val requestHeaders = request.headers
        val token = requestHeaders.getFirst(jwtProperties.tokenHeader)
        if (token != null) {
            val username = jwtUtil.getSubject(token)
            val securityContext = SecurityContextHolder.getContext()
            if (securityContext.authentication == null) {
                val userDetails = userDetailsService.loadUserByUsername(username)
                if (jwtUtil.validate(token, userDetails)) {
                    val authentication = UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
//                    authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
                    securityContext.authentication = authentication
                }
            }
        }
        return chain.filter(exchange)
    }

}