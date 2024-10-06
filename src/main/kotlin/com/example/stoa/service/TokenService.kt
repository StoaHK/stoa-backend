package com.example.stoa.service

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import java.util.UUID
import java.util.concurrent.TimeUnit

@Service
class TokenService(private val redisTemplate: RedisTemplate<String, String>) {
    fun generateTokenId(): String = UUID.randomUUID().toString()

    fun saveTokenMapping(tokenId: String, userId: Long, expirationTime: Long) {
        redisTemplate.opsForSet().add(userId.toString(), tokenId)
        redisTemplate.opsForValue().set(tokenId, userId.toString(), expirationTime, TimeUnit.MILLISECONDS)
    }

    fun getUserIdByTokenId(tokenId: String): Long? {
        val userIdStr = redisTemplate.opsForValue().get(tokenId)
        return userIdStr?.toLongOrNull()
    }

    fun removeTokenMapping(tokenId: String) {
        val userId = getUserIdByTokenId(tokenId)
        if (userId != null) {
            val userTokensKey = userId.toString()
            redisTemplate.opsForSet().remove(userTokensKey, tokenId)
        }
        redisTemplate.delete(tokenId)
    }

    fun removeAllUserTokens(userId: Long) {
        val userTokensKey = userId.toString()
        val tokens = redisTemplate.opsForSet().members(userTokensKey) ?: return

        tokens.forEach { token ->
            redisTemplate.delete(token)
        }

        redisTemplate.delete(userTokensKey)
    }
}