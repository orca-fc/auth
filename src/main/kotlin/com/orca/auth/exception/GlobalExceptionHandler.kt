package com.orca.auth.exception

import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebExceptionHandler
import reactor.core.publisher.Mono

@Component
class GlobalExceptionHandler: WebExceptionHandler {
    override fun handle(exchange: ServerWebExchange, ex: Throwable): Mono<Void> {
        if (ex is BaseException) {
            exchange.response.apply {
                statusCode = ex.httpStatus
                return writeWith(Mono.just(bufferFactory().wrap(ex.message!!.toByteArray())))
            }
        }

        return Mono.error(ex)
    }
}