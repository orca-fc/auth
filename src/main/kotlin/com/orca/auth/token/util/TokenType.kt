package com.orca.auth.token.util

enum class TokenType(val expiry: Long) {
    ACCESS(3600000L), // 1시간
    REFRESH(3600000L * 24) // 1일
}