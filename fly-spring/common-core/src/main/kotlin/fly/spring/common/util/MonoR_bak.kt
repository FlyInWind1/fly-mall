package fly.spring.common.util

import fly.spring.common.pojo.R
import reactor.core.CoreSubscriber
import reactor.core.Fuseable
import reactor.core.publisher.Mono
import reactor.core.publisher.Operators
import java.time.Duration


class MonoR_bak<T>(

        val value: R<T>

) : Mono<R<T>>(),
        Fuseable.ScalarCallable<R<T>>,
        Fuseable {

    override fun call(): R<T> {
        return value
    }

    override fun block(): R<T>? {
        return value
    }

    override fun block(timeout: Duration): R<T>? {
        return value
    }

    override fun subscribe(actual: CoreSubscriber<in R<T>>) {
        actual.onSubscribe(Operators.scalarSubscription(actual, value))
    }

    companion object {
        private val EMPTY_OK = ok(null)

        fun ok(): MonoR_bak<Nothing?> {
            return EMPTY_OK
        }

        fun <T> ok(data: T): MonoR_bak<T> {
            return onAssembly(MonoR_bak(R.ok(data))) as MonoR_bak<T>
        }

        fun <T> ok(data: T, msg: String): MonoR_bak<T> {
            return MonoR_bak(R.ok(data, msg))
        }

        fun <T> fail(data: T): MonoR_bak<T> {
            return MonoR_bak(R.fail(data))
        }
    }
}