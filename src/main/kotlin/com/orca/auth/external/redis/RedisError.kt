package com.orca.auth.external.redis

import org.springframework.http.HttpStatus

enum class RedisError(val status: HttpStatus, val message: String) {
    NOT_FOUND(HttpStatus.NOT_FOUND, "value not found. it is not exist key")
}