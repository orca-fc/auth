package com.orca.auth.token.service

import com.orca.auth.external.player.PlayerService
import com.orca.auth.external.redis.RedisService
import com.orca.auth.token.util.JwtManager
import com.orca.auth.utils.getDefaultPlayerResponse
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.springframework.mock.http.server.reactive.MockServerHttpRequest
import org.springframework.mock.web.server.MockServerWebExchange
import reactor.core.publisher.Mono

class TokenServiceTest : FunSpec({
    val jwtManager = mockk<JwtManager>()
    val redisService = mockk<RedisService>()
    val playerService = mockk<PlayerService>()
    val tokenService = TokenService(jwtManager, redisService, playerService)

    test("토큰 발급") {
        runTest {
            val exchange = MockServerWebExchange.from(MockServerHttpRequest.get(""))
            val id = "id"

            coEvery { playerService.findOneById(id) } returns Mono.just(getDefaultPlayerResponse())
            coEvery { jwtManager.issue(id, any()) } returns "token"
            coEvery { redisService.set(any(), any(), any()) } returns Unit

            val result = tokenService.generate(exchange, "id")

            result shouldBe "token"
            exchange.response.cookies["refresh"].shouldNotBeEmpty()
        }
    }
})
