package com.orca.auth.token.api

import com.orca.auth.token.service.TokenService
import com.orca.auth.util.baseResponse
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ServerWebExchange

@RequestMapping("/token")
@RestController
class TokenController(
    val tokenService: TokenService
) {
    @Operation(summary = "토큰 검증", description = "유효한 토큰인지 검증하는 API")
    @GetMapping
    suspend fun verify(@RequestParam token: String): ResponseEntity<TokenVerifyResponse> {
        val response = TokenVerifyResponse(
            userId = tokenService.verify(token),
            token = token
        )

        return baseResponse(
            body = response
        )
    }

    @Operation(summary = "토큰 발급", description = "새로운 토큰을 발급한다.\n 응답에는 accessToken이 포함되어있으며, refresh 토큰은 쿠키를 통해 반환한다.")
    @PostMapping
    suspend fun generate(
        @RequestParam userId: String,
        exchange: ServerWebExchange
    ): ResponseEntity<TokenGenerateResponse> {
        val response = TokenGenerateResponse(
            userId = userId,
            accessToken = tokenService.generate(exchange, userId)
        )

        return baseResponse(
            body = response
        )
    }

    @Operation(summary = "토큰 재발급", description = "토큰을 재발급 한다.")
    @PostMapping("/refresh")
    suspend fun refresh(
        @RequestBody request: TokenRefreshRequest,
        exchange: ServerWebExchange
    ): ResponseEntity<TokenRefreshResponse> {
        val newToken = tokenService.refresh(
            exchange = exchange,
            userId = request.userId,
            originToken = request.refreshToken
        )

        val response = TokenRefreshResponse(
            userId = request.userId,
            accessToken = newToken
        )

        return baseResponse(
            body = response
        )
    }
}