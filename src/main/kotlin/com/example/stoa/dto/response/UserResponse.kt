package com.example.stoa.dto.response

import com.example.stoa.model.UserRole

data class UserResponse(
    val username: String,
    val email: String,
    val role: UserRole
)