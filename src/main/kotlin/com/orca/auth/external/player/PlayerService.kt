package com.orca.auth.external.player

import com.orca.auth.exception.BaseException
import com.orca.auth.exception.ErrorCode
import com.orca.auth.external.config.WebClientFactory
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class PlayerService(
    clientFactory: WebClientFactory
) {
    val client = clientFactory.getClient("player")

    suspend fun findOneById(id: String): Mono<PlayerResponse> {
        return client.get()
            .uri {
                it.queryParam("id", id)
                    .build()
            }
            .retrieve()
            .bodyToMono(PlayerResponse::class.java)
            .onErrorResume {
                Mono.error(BaseException(ErrorCode.PLAYER_NOT_FOUND))
            }
    }
}