package com.example.stoa.model

import com.example.stoa.dto.response.UserResponse
import jakarta.persistence.*

@Entity
@Table(name = "users")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    var id: Long = 0,

    @Column(name = "username", length = 32, unique = false, nullable = false)
    val username: String,

    @Column(name = "email", length = 150, unique = true, nullable = false)
    val email: String,

    @Column(name = "password", length = 60, unique = false, nullable = false)
    val password: String,

    @Column(name = "role", unique = false, nullable = false)
    val role: UserRole = UserRole.MEMBER
) {
    fun toUserResponse() = UserResponse(
        username = username,
        email = email,
        role = role
    )
}