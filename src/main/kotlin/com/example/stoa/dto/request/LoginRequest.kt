package com.example.stoa.dto.request

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class LoginRequest(
    @field:NotBlank(message = "Email cannot be empty")
    @field:Size(message = "Email must between 3 to 150 characters", min = 3, max = 150)
    @field:Email(message = "Email is not valid")
    val email: String,

    @field:NotBlank(message = "Email cannot be empty")
    @field:Size(message = "Password must between 6 to 72 characters", min = 6, max = 72)
    val password: String
)