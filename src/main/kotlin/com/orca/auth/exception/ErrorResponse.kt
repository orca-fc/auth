package com.orca.auth.exception

class ErrorResponse(
    val code: String,
    val message: String,
    val timestamp: String,

    ) {
    val serviceName = "auth"

    constructor(ex: BaseException) : this(
        code = ex.code,
        message = ex.message,
        timestamp = ex.timeStamp,
    )
}