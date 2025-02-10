package com.orca.auth.external.redis

import com.orca.auth.exception.BaseException
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import java.time.Duration

@Service
class RedisService(
    private val redisTemplate: RedisTemplate<String, String>
) {
    suspend fun get(key: String): String? {
        return redisTemplate.opsForValue().get(key) ?: throw BaseException(RedisError.NOT_FOUND)
    }

    suspend fun set(key: String, value: String) {
        redisTemplate.opsForValue().set(key, value)
    }

    suspend fun setWithTTL(key: String, value: String, duration: Duration) {
        redisTemplate.opsForValue().set(key, value, duration)
    }

    suspend fun delete(key: String) {
         redisTemplate.delete(key)
    }
}