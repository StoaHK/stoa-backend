package com.example.stoa.dto.request

import com.example.stoa.model.User
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import org.springframework.security.crypto.password.PasswordEncoder

data class RegisterRequest(
    @field:NotBlank(message = "Username is required")
    @field:Size(min = 2, max = 20, message = "Username must be between 2 and 20 characters long")
    val username: String,

    @field:NotBlank(message = "Email is required")
    @field:Size(min = 3, max = 150, message = "Email must be between 3 and 150 characters long")
    @field:Email(message = "Please provide a valid email address")
    val email: String,

    @field:NotBlank(message = "Password is required")
    @field:Size(min = 6, max = 72, message = "Password must be between 6 and 72 characters long")
    val password: String
) {
    fun toAppUser(passwordEncoder: PasswordEncoder): User {
        return User(
            username = this.username,
            email = this.email,
            password = passwordEncoder.encode(this.password)
        )
    }
}