package com.orca.auth.token.service

import com.orca.auth.exception.BaseException
import com.orca.auth.external.redis.RedisService
import com.orca.auth.token.util.JwtManager
import com.orca.auth.token.util.TokenType
import io.jsonwebtoken.JwtException
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.springframework.http.ResponseCookie
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ServerWebExchange
import java.time.Duration

@Transactional(readOnly = true)
@Service
class TokenService(
    private val jwtManager: JwtManager,
    private val redisService: RedisService
) {

    suspend fun verify(token: String): String {
        return jwtManager.verify(token)
    }

    suspend fun generate(exchange: ServerWebExchange, userId: String): String {
        //TODO 유효한 userId인지 확인 (Member 서버) (비동기)
        val tokenMap = TokenType.entries.associateWith { jwtManager.generate(userId, it) }

        val accessToken = tokenMap[TokenType.ACCESS]!!
        val refreshToken = tokenMap[TokenType.REFRESH]!!

        coroutineScope {
            launch {
                redisService.setWithTTL(
                    userId,
                    refreshToken,
                    Duration.ofMillis(TokenType.REFRESH.expiry)
                )
            }
        }

        exchange.response.addCookie(
            ResponseCookie.from("refresh", refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(Duration.ofDays(1))
                .build()
        )

        return accessToken
    }

    suspend fun refresh(exchange: ServerWebExchange, userId: String, originToken: String): String {
        val tokenFromRedis = redisService.get(userId)

        if (tokenFromRedis == originToken) {
            return generate(exchange, userId)
        } else {
            throw BaseException(JwtException("유효하지 않은 refresh 토큰 입니다."))
        }
    }
}