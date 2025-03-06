package com.orca.auth.token.util

import com.orca.auth.exception.AuthError
import com.orca.auth.exception.BaseException
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.security.Keys
import io.jsonwebtoken.security.SignatureException
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.*

@Service
class JwtManager {

    @Value("\${token.jwt-secret}")
    private lateinit var secret: String

    private val key by lazy { Keys.hmacShaKeyFor(secret.toByteArray()) }
    suspend fun generate(userId: String, type: TokenType): String {
        val now = Date()
        return Jwts.builder().apply {
            subject(userId)
            issuedAt(now)
            expiration(Date(now.time + type.expiry))
            signWith(key, Jwts.SIG.HS256)
        }.compact()
    }

    suspend fun verify(token: String): String {
        return try {
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token).payload.subject
        } catch (e: JwtException) {
            throw BaseException(
                when (e) {
                    is ExpiredJwtException -> AuthError.TOKEN_EXPIRED
                    is SignatureException -> AuthError.INVALID_TOKEN
                    is MalformedJwtException -> AuthError.MALFORMED_TOKEN
                    else -> AuthError.JWT_EXCEPTION
                }
            )
        }
    }

}