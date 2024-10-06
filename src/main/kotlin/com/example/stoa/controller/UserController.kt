package com.example.stoa.controller

import com.example.stoa.dto.request.RegisterRequest
import com.example.stoa.dto.response.UserResponse
import com.example.stoa.model.User
import com.example.stoa.service.TokenService
import com.example.stoa.service.UserService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/api/users")
@Validated
class UserController(
    private val userService: UserService,
    private val passwordEncoder: PasswordEncoder,
    private val tokenService: TokenService
) {
    fun getContext(): SecurityContext = SecurityContextHolder.getContext()

    @PostMapping("/register")
    fun registerUser(@Valid @RequestBody request: RegisterRequest): ResponseEntity<Void> {
        if (userService.existsByEmail(request.email)) {
            throw ResponseStatusException(HttpStatus.CONFLICT, "User email already exists")
        }
        val appUser = request.toAppUser(passwordEncoder)
        userService.create(appUser)
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @GetMapping("/me")
    fun fetchUserProfile(): ResponseEntity<UserResponse> {
        val user = getContext().authentication.principal as User
        return ResponseEntity.ok(user.toUserResponse())
    }

    @DeleteMapping("/me")
    fun deleteUser(): ResponseEntity<Void> {
        val context = getContext()
        val user = context.authentication.principal as User
        userService.deleteById(user.id)
        tokenService.removeAllUserTokens(user.id)
        return ResponseEntity.ok().build()
    }
}