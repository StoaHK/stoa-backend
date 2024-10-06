package com.example.stoa.service

import com.example.stoa.model.User
import com.example.stoa.repository.UserRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository
) {
    fun existsByEmail(@Param("email") email: String) = userRepository.existsByEmail(email)

    fun findByEmail(email: String) = userRepository.findByEmail(email)
    fun findById(id: Long): User? = userRepository.findById(id).orElse(null)

    fun deleteById(@Param("id") id: Long) = userRepository.deleteById(id)

    fun create(user: User) = userRepository.save(user)
}