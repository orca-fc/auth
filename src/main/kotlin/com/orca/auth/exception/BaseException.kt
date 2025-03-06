package com.orca.auth.exception

import com.orca.auth.util.getCurrentTimestamp
import org.springframework.http.HttpStatus

class BaseException(
    val httpStatus: HttpStatus,
    val code: String,
    override val message: String,
) : RuntimeException() {

    val timeStamp = getCurrentTimestamp()

    constructor(e: AuthError) : this(
        httpStatus = e.status!!,
        code = e.name,
        message = e.message,
    )
}