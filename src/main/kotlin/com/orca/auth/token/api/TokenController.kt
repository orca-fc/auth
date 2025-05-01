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

    @Operation(summary = "토큰 재발급", description = "토큰을 재발급 한다.")
    @PostMapping("/refresh")
    suspend fun refresh(
        @RequestParam loginId: String,
        @CookieValue("refresh") refreshToken: String,
        exchange: ServerWebExchange
    ): ResponseEntity<String> {
        val newToken = tokenService.refresh(
            exchange = exchange,
            loginId = loginId,
            clientToken = refreshToken
        )

        return baseResponse(body = newToken)
    }
}