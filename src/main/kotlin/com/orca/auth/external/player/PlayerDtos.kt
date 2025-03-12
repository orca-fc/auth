package com.orca.auth.external.player

data class PlayerResponse(
    val id: String,
    val name: String,
    val birth: String,
    val clubHistories: List<String>,
)