package com.example.stoa.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import java.security.SecureRandom
import java.util.*

@ConfigurationProperties(prefix = "jwt")
class JwtConfig(
    secret: String = run {
        val key = Base64.getEncoder().encodeToString(SecureRandom().generateSeed(32))
        println("No jwt secret provided! Auto generated.")
        println("Secret key: $key")
        return@run key
    },
    val accessTokenExpiration: Long
) {
    val secret: String = run {
        val size = Base64.getDecoder().decode(secret).size
        if (size == 32) return@run secret
        else throw RuntimeException("JWT Secret byte size not equal to 32! ($size found)")
    }
}