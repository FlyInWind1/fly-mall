package fly.spring.common.util

import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono

class RequestContextUtils : WebFilter {

    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        return chain.filter(exchange)
            .contextWrite { ctx -> ctx.put(CONTEXT_KEY, exchange.request) }
    }

    companion object {
        val CONTEXT_KEY = ServerHttpRequest::class

        /**
         * Gets the {@code Mono<ServerHttpRequest>} from Reactor {@link Context}
         * @return the {@code Mono<ServerHttpRequest>}
         */
        fun getRequest(): Mono<ServerHttpRequest> {
            return Mono.deferContextual { contextView -> contextView.get(CONTEXT_KEY) }
        }
    }
}
