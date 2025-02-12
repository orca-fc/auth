package com.orca.auth.exception

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebExceptionHandler
import reactor.core.publisher.Mono

@Order(-2)
@Component
class GlobalExceptionHandler(
    private val objectMapper: ObjectMapper
) : WebExceptionHandler {
    override fun handle(exchange: ServerWebExchange, ex: Throwable): Mono<Void> {
        val errorResponse = if (ex is BaseException) {
            ErrorResponse(ex)
        } else {
            ErrorResponse.default()
        }

        return exchange.response.run {
            statusCode = errorResponse.status
            writeWith(
                Mono.just(bufferFactory().wrap(objectMapper.writeValueAsBytes(errorResponse)))
            )
        }
    }
}