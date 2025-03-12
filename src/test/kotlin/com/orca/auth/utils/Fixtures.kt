package com.orca.auth.utils

import com.orca.auth.external.player.PlayerResponse

fun getDefaultPlayerResponse(): PlayerResponse {
    return PlayerResponse(
        id = "id",
        name = "name",
        birth = "111111",
        clubHistories = emptyList()
    )
}