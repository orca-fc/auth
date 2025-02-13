package com.orca.auth.exception

import com.orca.auth.external.redis.RedisError
import com.orca.auth.util.getCurrentTimestamp
import io.jsonwebtoken.JwtException
import org.springframework.http.HttpStatus

class BaseException(val httpStatus: HttpStatus, message: String, timeStamp: String): RuntimeException() {
    constructor(e: RedisError): this(
        httpStatus = e.status,
        message = e.message,
        timeStamp = getCurrentTimestamp()
    )

    constructor(e: JwtException): this(
        httpStatus = HttpStatus.UNAUTHORIZED,
        message = e.message!!,
        timeStamp = getCurrentTimestamp()
    )
}