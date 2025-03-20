package com.orca.auth.external.player

import com.orca.auth.authentication.api.PlayerVerifyResponse
import com.orca.auth.exception.BaseException
import com.orca.auth.exception.ErrorCode
import com.orca.auth.external.config.WebClientFactory
import org.springframework.http.HttpStatusCode
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.awaitBody
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

    suspend fun generate(name: String, birth: String, loginId: String, password: String): PlayerResponse {
        return client.post()
            .bodyValue(
                GenerateRequest(
                    name = name,
                    birth = birth,
                    loginId = loginId,
                    password = BCryptPasswordEncoder().encode(password)
                )
            ).retrieve()
            .onStatus(HttpStatusCode::isError) {
                throw BaseException(ErrorCode.DUPLICATED_LOGIN_ID)
            }
            .awaitBody<PlayerResponse>()
    }

    suspend fun login(loginId: String, password: String) {
        val player = client.get()
            .uri {
                it.path("/credentials")
                    .queryParam("loginId", loginId)
                    .build()
            }.retrieve()
            .onStatus(HttpStatusCode::isError) {
                throw BaseException(ErrorCode.PLAYER_NOT_FOUND)
            }.awaitBody<PlayerVerifyResponse>()

        val isValid = BCryptPasswordEncoder().matches(password, player.encryptedPassword)
        if (!isValid) throw BaseException(ErrorCode.LOGIN_FAILED)
    }
}