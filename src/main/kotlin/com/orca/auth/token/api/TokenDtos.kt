package com.orca.auth.token.api

data class TokenVerifyResponse(
    val userId: String?,
    val token: String
)

data class TokenGenerateResponse(
    val userId: String,
    val accessToken: String
)

data class TokenRefreshRequest(
    val userId: String,
    val refreshToken: String
)

data class TokenRefreshResponse(
    val userId: String,
    val accessToken: String
)