package com.example.stoa.model

data class JwtUser(
    val user: User,
    val tokenId: String
)