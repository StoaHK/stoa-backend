package com.example.stoa.model

import com.example.stoa.dto.response.UserResponse
import jakarta.persistence.*

@Entity
@Table(name = "users")
data class User(
    @field:Id
    @field:GeneratedValue(strategy = GenerationType.IDENTITY)
    @field:Column(name = "id", nullable = false)
    var id: Long = 0,

    @field:Column(name = "name", length = 32, unique = false, nullable = false)
    val name: String,

    @field:Column(name = "email", length = 254, unique = true, nullable = false)
    val email: String,

    @field:Column(name = "password", length = 60, unique = false, nullable = false)
    val password: String,

    @field:Column(name = "role", unique = false, nullable = false)
    val role: UserRole = UserRole.MEMBER
) {
    fun toUserResponse() = UserResponse(
        username = name,
        email = email,
        role = role
    )
}