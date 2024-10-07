package com.example.stoa.service

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import java.util.UUID
import java.util.concurrent.TimeUnit

@Service
class TokenService(
    private val redisTemplate: RedisTemplate<String, String>
) {
    companion object {
        private const val TOKEN_PREFIX = "token:"
        private const val USER_PREFIX = "user:"
    }

    fun generateTokenId(): String = UUID.randomUUID().toString()

    fun saveTokenMapping(tokenId: String, userId: Long, expirationMillis: Long) {
        val tokenKey = "${TOKEN_PREFIX}$tokenId"
        val userKey = "${USER_PREFIX}$userId"

        redisTemplate.opsForValue().set(tokenKey, userId.toString(), expirationMillis, TimeUnit.MILLISECONDS)
        redisTemplate.opsForSet().add(userKey, tokenId)
    }

    fun getUserIdByTokenId(tokenId: String): Long? {
        val userIdStr = redisTemplate.opsForValue().get("${TOKEN_PREFIX}$tokenId") ?: return null
        return userIdStr.toLongOrNull()
    }

    fun removeTokenMapping(tokenId: String) {
        val userId = getUserIdByTokenId(tokenId) ?: return
        val tokenKey = "${TOKEN_PREFIX}$tokenId"
        val userKey = "${USER_PREFIX}$userId"

        redisTemplate.delete(tokenKey)
        redisTemplate.opsForSet().remove(userKey, tokenId)
    }

    fun removeAllUserTokens(userId: Long) {
        val userKey = "${USER_PREFIX}$userId"

        val tokenIds = redisTemplate.opsForSet().members(userKey) ?: return

        tokenIds.forEach { tokenId ->
            redisTemplate.delete("${TOKEN_PREFIX}$tokenId")
        }

        redisTemplate.delete(userKey)
    }

    fun isTokenValid(tokenId: String): Boolean {
        return redisTemplate.hasKey("${TOKEN_PREFIX}$tokenId")
    }
}