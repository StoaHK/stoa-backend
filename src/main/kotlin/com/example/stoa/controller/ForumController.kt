package com.example.stoa.controller

import com.example.stoa.dto.request.CreateThreadRequest
import com.example.stoa.dto.request.UpdateThreadRequest
import com.example.stoa.dto.response.ThreadResponse
import com.example.stoa.dto.response.toThreadResponse
import com.example.stoa.model.User
import com.example.stoa.service.ForumService
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/forum")
@Validated
class ForumController(
    private val forumService: ForumService
) {
    private fun getCurrentUser(): User =
        SecurityContextHolder.getContext().authentication.principal as User

    @GetMapping("/threads")
    fun getThreads(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int
    ): ResponseEntity<Page<ThreadResponse>> {
        val pageable = PageRequest.of(page, size, Sort.by("isSticky").descending().and(Sort.by("createdAt").descending()))
        val threads = forumService.getAllThreads(pageable)
        return ResponseEntity.ok(threads.map { it.toThreadResponse() })
    }

    @GetMapping("/threads/{id}")
    fun getThread(@PathVariable id: Long): ResponseEntity<ThreadResponse> {
        val thread = forumService.getThreadById(id)
        forumService.incrementViewCount(id)
        return ResponseEntity.ok(thread.toThreadResponse())
    }

    @PostMapping("/threads")
    fun createThread(
        @Valid @RequestBody request: CreateThreadRequest
    ): ResponseEntity<ThreadResponse> {
        val thread = forumService.createThread(request, getCurrentUser())
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(thread.toThreadResponse())
    }

    @PutMapping("/threads/{id}")
    fun updateThread(
        @PathVariable id: Long,
        @Valid @RequestBody request: UpdateThreadRequest
    ): ResponseEntity<ThreadResponse> {
        val thread = forumService.updateThread(id, request, getCurrentUser())
        return ResponseEntity.ok(thread.toThreadResponse())
    }

    @DeleteMapping("/threads/{id}")
    fun deleteThread(@PathVariable id: Long): ResponseEntity<Void> {
        forumService.deleteThread(id, getCurrentUser())
        return ResponseEntity.noContent().build()
    }
}