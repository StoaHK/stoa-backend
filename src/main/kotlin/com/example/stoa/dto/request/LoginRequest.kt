package com.example.stoa.dto.request

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class LoginRequest(
    @field:NotBlank(message = "{email.notblank}")
    @field:Size(min = 3, max = 150, message = "{email.size}")
    @field:Email(message = "{email.invalid}")
    val email: String,

    @field:NotBlank(message = "{password.notblank}")
    @field:Size(min = 6, max = 72, message = "{password.size}")
    val password: String
)