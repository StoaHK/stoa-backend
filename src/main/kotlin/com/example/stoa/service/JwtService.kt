package com.example.stoa.service

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import com.example.stoa.configuration.JwtConfig
import com.example.stoa.model.JwtUser
import com.example.stoa.model.User
import org.springframework.stereotype.Service
import java.util.*
import javax.crypto.SecretKey

@Service
class JwtService(
    private val jwtConfig: JwtConfig,
    private val tokenService: TokenService,
    private val userService: UserService
) {
    private val secretKey: SecretKey = Keys.hmacShaKeyFor(jwtConfig.secret.toByteArray())

    fun generate(user: User): String {
        val tokenId = tokenService.generateTokenId()
        val expirationTime = System.currentTimeMillis() + jwtConfig.accessTokenExpiration
        tokenService.saveTokenMapping(tokenId, user.id, jwtConfig.accessTokenExpiration)

        return Jwts.builder()
            .subject(tokenId)
            .expiration(Date(expirationTime))
            .signWith(secretKey)
            .compact()
    }

    fun getUser(token: String): JwtUser? {
        val tokenId = extractTokenId(token) ?: return null
        if (isExpired(token)) return null
        val userId = tokenService.getUserIdByTokenId(tokenId) ?: return null
        val user = userService.findById(userId) ?: return null
        return JwtUser(user, tokenId)
    }

    fun extractTokenId(token: String): String? = getAllClaims(token).subject

    fun isExpired(token: String): Boolean =
        getAllClaims(token).expiration.before(Date(System.currentTimeMillis()))

    private fun getAllClaims(token: String): Claims {
        val parser = Jwts.parser()
            .verifyWith(secretKey)
            .build()
        return parser
            .parseSignedClaims(token)
            .payload
    }
}