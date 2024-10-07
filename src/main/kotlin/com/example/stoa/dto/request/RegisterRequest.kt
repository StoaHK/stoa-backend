package com.example.stoa.dto.request

import com.example.stoa.model.User
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import org.springframework.security.crypto.password.PasswordEncoder

data class RegisterRequest(
    @field:NotBlank(message = "{username.notblank}")
    @field:Size(min = 2, max = 20, message = "{username.size}")
    val username: String,

    @field:NotBlank(message = "{email.notblank}")
    @field:Size(min = 3, max = 150, message = "{email.size}")
    @field:Email(message = "{email.invalid}")
    val email: String,

    @field:NotBlank(message = "{password.notblank}")
    @field:Size(min = 6, max = 72, message = "{password.size}")
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