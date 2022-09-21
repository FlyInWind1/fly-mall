package fly.spring.common.kotlin.extension

import fly.spring.common.pojo.R
import fly.spring.common.util.MonoR
import reactor.core.publisher.Mono

fun <T> T.mapOk(): Mono<R<T>> = MonoR.ok(this)

fun <T> T.mapOk(msg: String): Mono<R<T>> = MonoR.ok(this, msg)

fun <T> T.mapFail(): Mono<R<T>> = MonoR.fail(this)

fun <T> T.mapFail(msg: String): Mono<R<T>> = MonoR.fail(this, msg)

fun <T> Mono<T>.mapOk(): Mono<R<T>> = this.map { R.ok(it) }

fun <T> Mono<*>.rok(): Mono<R<T?>> = this.thenReturn(R.okc())
