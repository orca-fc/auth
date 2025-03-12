package com.orca.auth.external.redis

import com.orca.auth.exception.BaseException
import com.orca.auth.exception.ErrorCode
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.stereotype.Service
import java.time.Duration

@Service
class RedisService(
    private val redisTemplate: ReactiveRedisTemplate<String, String>
) {
    val serviceName: String = "auth"
    suspend fun get(prefix: String, key: String): String? {
        return redisTemplate.opsForValue().get("${serviceName}:${prefix}:${key}").awaitSingleOrNull()
            ?: throw BaseException(ErrorCode.KEY_NOT_FOUND)
    }

    suspend fun set(prefix: String, key: String, value: String, duration: Duration = Duration.ofDays(1)) {
        redisTemplate.opsForValue().set("${serviceName}:${prefix}:${key}", value, duration).awaitSingle()
    }

    suspend fun delete(prefix: String, key: String) {
        redisTemplate.delete("${serviceName}:${prefix}:${key}").awaitSingle()
    }
}