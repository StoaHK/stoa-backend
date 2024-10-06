package com.example.stoa.controller

import com.example.stoa.dto.response.AuthResponse
import com.example.stoa.dto.request.LoginRequest
import com.example.stoa.service.JwtService
import com.example.stoa.service.TokenService
import com.example.stoa.service.UserService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/api/auth")
@Validated
class AuthController(
    private val userService: UserService,
    private val jwtService: JwtService,
    private val tokenService: TokenService,
    private val passwordEncoder: PasswordEncoder
) {
    @PostMapping("/login")
    fun login(@Valid @RequestBody request: LoginRequest): ResponseEntity<AuthResponse> {
        val user = userService.findByEmail(request.email)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid email or password")
        if (!passwordEncoder.matches(request.password, user.password))
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid email or password")
        val jwtToken = jwtService.generate(user)
        return ResponseEntity.ok(AuthResponse(jwtToken))
    }

    @GetMapping("/logout")
    fun logout(): ResponseEntity<Void> {
        val context = SecurityContextHolder.getContext()
        val tokenId = context.authentication.credentials as String
        tokenService.removeTokenMapping(tokenId)
        return ResponseEntity.ok().build()
    }
}