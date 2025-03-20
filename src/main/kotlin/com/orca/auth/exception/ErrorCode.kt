package com.orca.auth.exception

import org.springframework.http.HttpStatus

enum class ErrorCode(val status: HttpStatus? = HttpStatus.NOT_FOUND, val message: String) {
    UNDEFINED_EXCEPTION(status = HttpStatus.INTERNAL_SERVER_ERROR, message = "Sorry, undefined exception"),
    LOGIN_FAILED(status = HttpStatus.BAD_REQUEST, message = "Login failed. check id or password"),

    // redis
    KEY_NOT_FOUND(message = "Redis key not found"),

    // jwt
    TOKEN_EXPIRED(status = HttpStatus.UNAUTHORIZED, message = "Expired token"),
    INVALID_TOKEN(status = HttpStatus.FORBIDDEN, message = "Invalid token"),
    MALFORMED_TOKEN(status = HttpStatus.BAD_REQUEST, message = "Malformed token"),
    JWT_EXCEPTION(status = HttpStatus.BAD_REQUEST, message = "JWT exception"),

    // external
    PLAYER_NOT_FOUND(status = HttpStatus.BAD_REQUEST, message = "Player not found. Invalid id"),
    DUPLICATED_LOGIN_ID(status = HttpStatus.BAD_REQUEST, message = "Login id is duplicated.")
}