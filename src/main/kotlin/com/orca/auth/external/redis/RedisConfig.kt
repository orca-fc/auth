package com.orca.auth.external.redis

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.cache.RedisCacheConfiguration
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.StringRedisSerializer
import java.time.Duration.ofDays

@Configuration
class RedisConfig {
    @Value("\${redis.host}")
    val host: String = "localhost"

    @Value("\${redis.port}")
    val port: String = "6379"

    @Bean
    fun redisTemplate(): RedisTemplate<String, String> {
        return RedisTemplate<String, String>()
            .apply {
                connectionFactory = redisConnectionFactory()
                keySerializer = StringRedisSerializer()
                valueSerializer = StringRedisSerializer()
            }
    }

    @Bean
    fun redisConnectionFactory(): LettuceConnectionFactory {
        return LettuceConnectionFactory(host, port.toInt())
    }

    @Bean
    fun redisCacheManager(redisTemplate: RedisTemplate<String, String>): RedisCacheManager {
        val redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
            .entryTtl(ofDays(1))
            .serializeValuesWith(
                RedisSerializationContext.SerializationPair.fromSerializer(
                    GenericJackson2JsonRedisSerializer()
                )
            )

        return RedisCacheManager.builder(redisConnectionFactory())
            .cacheDefaults(redisCacheConfiguration)
            .build()
    }
}