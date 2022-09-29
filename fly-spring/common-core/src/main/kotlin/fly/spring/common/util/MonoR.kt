package fly.spring.common.util

import fly.spring.common.pojo.R
import reactor.core.publisher.Mono


class MonoR {
    companion object {
        fun <T> ok(): Mono<R<T?>> {
            return Mono.just(R.ok())
        }

        fun <T> ok(data: T): Mono<R<T>> {
            return Mono.just(R.ok(data))
        }

        fun <T> ok(data: T, msg: String): Mono<R<T>> {
            return Mono.just(R.ok(data, msg))
        }

        fun <T> fail(): Mono<R<T?>> {
            return Mono.just(R.fail())
        }

        fun <T> fail(data: T): Mono<R<T>> {
            return Mono.just(R.fail(data))
        }

        fun <T> fail(data: T, msg: String): Mono<R<T>> {
            return Mono.just(R.fail(data, msg))
        }
    }
}
