package fly.spring.common.kotlin.extension

import fly.spring.common.pojo.R
import fly.spring.common.util.MonoR
import reactor.core.publisher.Mono

fun <T> T.rok(): Mono<R<T>> = MonoR.ok(this)

fun <T> T.rok(msg: String): Mono<R<T>> = MonoR.ok(this, msg)

fun <T> T.rfa(): Mono<R<T>> = MonoR.fail(this)

fun <T> T.rfa(msg: String): Mono<R<T>> = MonoR.fail(this, msg)

fun <T> Mono<T>.rok(): Mono<R<T>> = this.map { R.ok(it) }
