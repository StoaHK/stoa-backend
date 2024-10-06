package com.example.stoa.dto.request

import com.example.stoa.model.User
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import org.springframework.security.crypto.password.PasswordEncoder

data class RegisterRequest(
    @field:NotBlank(message = "Username cannot be empty")
    @field:Size(message = "Username must between 2 and 20 characters", min = 2, max = 20)
    val username: String,

    @field:NotBlank(message = "Email cannot be empty")
    @field:Size(message = "Email must between 3 to 150 characters", min = 3, max = 150)
    @field:Email(message = "Email is not valid")
    val email: String,

    @field:NotBlank(message = "Email cannot be empty")
    @field:Size(message = "Password must between 6 to 72 characters", min = 6, max = 72)
    val password: String
) {
    fun toAppUser(passwordEncoder: PasswordEncoder): User {
        return User(
            name = this.username,
            email = this.email,
            password = passwordEncoder.encode(this.password)
        )
    }
}