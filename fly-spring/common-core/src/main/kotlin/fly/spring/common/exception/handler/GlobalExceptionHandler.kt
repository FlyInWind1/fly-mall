package fly.spring.common.exception.handler

import com.fasterxml.jackson.core.JsonParseException
import fly.spring.common.exception.GeneralException
import fly.spring.common.exception.LoginException
import fly.spring.common.pojo.R
import fly.spring.common.util.MonoR
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import reactor.core.publisher.Mono

@RestControllerAdvice
class GlobalExceptionHandler {
    companion object {
        val log = KotlinLogging.logger { }
    }

    @ExceptionHandler(GeneralException::class)
    fun generalException(e: GeneralException): Mono<R<String>> {
        return MonoR.fail(e.respMessage)
    }

    @ExceptionHandler(JsonParseException::class, org.springframework.boot.json.JsonParseException::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun jsonParseException(e: Exception): Mono<R<String?>> {
        log.warn(e) { e.message }
        return MonoR.fail(e.message)
    }

    @ExceptionHandler(Exception::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun exception(e: Exception): Mono<R<String?>> {
        log.warn(e) { e.message }
        return MonoR.fail(e.message)
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(LoginException::class)
    fun loginException(e: LoginException): Mono<R<String>> {
        return generalException(e)
    }

}
