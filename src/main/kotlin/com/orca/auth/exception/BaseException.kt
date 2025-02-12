package com.orca.auth.exception

import com.orca.auth.external.redis.RedisError
import com.orca.auth.util.getCurrentTimestamp
import io.jsonwebtoken.JwtException
import org.springframework.http.HttpStatus

class BaseException(val httpStatus: HttpStatus, override val message: String) : RuntimeException() {
    val timestamp: String = getCurrentTimestamp()

    constructor(e: RedisError) : this(
        httpStatus = e.status,
        message = e.message,
    )

    constructor(e: JwtException) : this(
        httpStatus = HttpStatus.UNAUTHORIZED,
        message = e.message!!,
    )
}

class ErrorResponse(
    val status: HttpStatus,
    val message: String,
    val timestamp: String
) {
    constructor(e: BaseException) : this(
        status = e.httpStatus,
        message = e.message,
        timestamp = e.timestamp
    )

    companion object {
        fun default(): ErrorResponse {
            return ErrorResponse(
                status = HttpStatus.INTERNAL_SERVER_ERROR,
                message = "UNDEFINED EXCEPTION",
                timestamp = getCurrentTimestamp()
            )
        }
    }
}