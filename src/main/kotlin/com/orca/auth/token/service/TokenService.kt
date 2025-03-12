package com.orca.auth.token.service

import com.orca.auth.exception.ErrorCode
import com.orca.auth.exception.BaseException
import com.orca.auth.external.player.PlayerService
import com.orca.auth.external.redis.RedisService
import com.orca.auth.token.util.JwtManager
import com.orca.auth.token.util.TokenType
import kotlinx.coroutines.*
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.http.ResponseCookie
import org.springframework.stereotype.Service
import org.springframework.web.server.ServerWebExchange
import java.time.Duration

@Service
class TokenService(
    private val jwtManager: JwtManager,
    private val redisService: RedisService,
    private val playerService: PlayerService
) {

    suspend fun verify(token: String): String {
        return jwtManager.verify(token)
    }

    suspend fun generate(exchange: ServerWebExchange, userId: String): String {
        return coroutineScope {
            val validatePlayer = async { playerService.findOneById(userId).awaitSingle() }
            val tokens = async { jwtManager.issue(userId, TokenType.ACCESS) to jwtManager.issue(userId, TokenType.REFRESH) }

            try {
                validatePlayer.await()
            } catch (e: BaseException) {
                throw BaseException(ErrorCode.REGISTRATION_FAIL)
            }

            val (accessToken, refreshToken) = tokens.await()

            launch { cachingToken(prefix = "refresh", key = userId, value = refreshToken) }
            async { setTokenByCookie(exchange, refreshToken) }.await()

            accessToken
        }
    }

    suspend fun setTokenByCookie(exchange: ServerWebExchange, token: String) {
        exchange.response.addCookie(
            ResponseCookie.from("refresh", token)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(Duration.ofDays(1))
                .build()
        )
    }

    suspend fun cachingToken(prefix: String, key: String, value: String) {
        redisService.set(
            prefix = prefix,
            key = key,
            value = value,
            duration = Duration.ofMillis(TokenType.REFRESH.expiry)
        )
    }

    suspend fun refresh(exchange: ServerWebExchange, userId: String, clientToken: String): String {
        return coroutineScope {
            val tokenFromRedis = async { redisService.get(prefix = "refresh", key = userId) }.await()

            if (clientToken == tokenFromRedis) {
                val (accessToken, refreshToken) =
                    async { jwtManager.issue(userId, TokenType.ACCESS) to jwtManager.issue(userId, TokenType.REFRESH) }.await()
                
                launch { cachingToken(prefix = "refresh", key = userId, value = refreshToken) }
                async { setTokenByCookie(exchange, refreshToken) }.await()
                accessToken
            } else {
                throw BaseException(ErrorCode.INVALID_TOKEN)
            }
        }
    }
}