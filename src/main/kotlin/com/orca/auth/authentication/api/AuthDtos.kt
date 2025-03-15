package com.orca.auth.authentication.api

data class SignUpRequest(
    val name: String,
    val birth: String,
    val loginId: String,
    val password: String
)

data class SignUpResponse(
    val id: String,
    val name: String,
    val birth: String,
    val loginId: String
)

data class SignInRequest(
    val loginId: String,
    val password: String
)

data class PlayerVerifyResponse(
    val id: String,
    val loginId: String,
    val encryptedPassword: String
)