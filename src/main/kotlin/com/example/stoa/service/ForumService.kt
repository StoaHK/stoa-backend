package com.example.stoa.service

import com.example.stoa.dto.request.CreateThreadRequest
import com.example.stoa.dto.request.UpdateThreadRequest
import com.example.stoa.model.ForumThread
import com.example.stoa.model.User
import com.example.stoa.model.UserRole
import com.example.stoa.repository.ForumRepository
import jakarta.transaction.Transactional
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class ForumService(
    private val forumRepository: ForumRepository
) {
    fun getAllThreads(pageable: Pageable): Page<ForumThread> =
        forumRepository.findAllThreads(pageable)

    fun getThreadById(id: Long): ForumThread =
        forumRepository.findById(id).orElseThrow {
            ResponseStatusException(HttpStatus.NOT_FOUND, "Thread not found")
        }

    @Transactional
    fun incrementViewCount(id: Long) {
        forumRepository.incrementViewCount(id)
    }

    fun createThread(request: CreateThreadRequest, user: User): ForumThread {
        val thread = ForumThread(
            id = 0,
            user = user,
            title = request.title,
            content = request.content,
            isSticky = false
        )
        return forumRepository.save(thread)
    }

    @Transactional
    fun updateThread(id: Long, request: UpdateThreadRequest, user: User): ForumThread {
        val thread = getThreadById(id)
        if (thread.user.id != user.id && user.role != UserRole.ADMIN) {
            throw ResponseStatusException(HttpStatus.FORBIDDEN, "You don't have permission to update this thread")
        }

        val updatedThread = thread.copy(
            title = request.title,
            content = request.content,
            isSticky = thread.isSticky,
            isClosed = request.isClosed
        )
        return forumRepository.save(updatedThread)
    }

    @Transactional
    fun deleteThread(id: Long, user: User) {
        val thread = getThreadById(id)
        if (thread.user.id != user.id && user.role != UserRole.ADMIN) {
            throw ResponseStatusException(HttpStatus.FORBIDDEN, "You don't have permission to delete this thread")
        }
        forumRepository.delete(thread)
    }
}