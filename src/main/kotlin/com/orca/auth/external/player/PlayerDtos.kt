package com.orca.auth.external.player

data class PlayerResponse(
    val id: String,
    val name: String,
    val birth: String,
    val loginId: String,
    val clubHistories: List<String>?,
)

data class GenerateRequest(
    val name: String,
    val birth: String,
    val loginId: String,
    val password: String
)