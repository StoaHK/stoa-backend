package com.example.stoa.model

import com.example.stoa.dto.response.UserResponse
import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "users")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    var id: Long = 0,

    @Column(name = "username", length = 32, nullable = false)
    val username: String,

    @Column(name = "email", length = 150, unique = true, nullable = false)
    val email: String,

    @Column(name = "password", length = 60, nullable = false)
    val password: String,

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    val forumThreads: Set<ForumThread> = setOf(),

    @Column(name = "created_at", nullable = false)
    val createdAt: Instant = Instant.now(),

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    val role: UserRole = UserRole.MEMBER
) {
    override fun toString() = "User(id=$id, username='$username', email='$email', role=$role)"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User

        if (id != other.id) return false
        if (username != other.username) return false
        if (email != other.email) return false
        if (password != other.password) return false
        if (createdAt != other.createdAt) return false
        if (role != other.role) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + username.hashCode()
        result = 31 * result + email.hashCode()
        result = 31 * result + password.hashCode()
        result = 31 * result + createdAt.hashCode()
        result = 31 * result + role.hashCode()
        return result
    }

    fun toUserResponse() = UserResponse(
        username = username,
        email = email,
        role = role
    )
}