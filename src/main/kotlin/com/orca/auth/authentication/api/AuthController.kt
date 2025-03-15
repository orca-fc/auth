package com.orca.auth.authentication.api

import com.orca.auth.external.player.PlayerService
import com.orca.auth.token.service.TokenService
import com.orca.auth.util.baseResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ServerWebExchange

@RequestMapping
@RestController
class AuthController(
    private val playerService: PlayerService,
    private val tokenService: TokenService
) {
    @PostMapping("/registrations")
    suspend fun signUp(@RequestBody request: SignUpRequest): ResponseEntity<SignUpResponse> {
        val player = playerService.generate(
            name = request.name,
            birth = request.birth,
            loginId = request.loginId,
            password = request.password
        )

        return baseResponse(
            body = SignUpResponse(
                id = player.id,
                name = player.name,
                birth = player.birth,
                loginId = player.loginId
            )
        )
    }

    @GetMapping("/login")
    suspend fun signIn(
        @RequestBody request: SignInRequest, exchange: ServerWebExchange
    ): ResponseEntity<String> {
        playerService.login(request.loginId, request.password)

        return baseResponse(
            body = tokenService.generate(exchange, request.loginId)
        )
    }
}