package com.example.stoa.dto.response

import com.example.stoa.model.ForumThread
import java.time.Instant

data class ThreadResponse(
    val id: Long,
    val title: String,
    val content: String,
    val author: UserResponse,
    val isSticky: Boolean,
    val isClosed: Boolean,
    val createdAt: Instant,
    val viewCount: Int
)

fun ForumThread.toThreadResponse() = ThreadResponse(
    id = id,
    title = title,
    content = content,
    author = user.toUserResponse(),
    isSticky = isSticky,
    isClosed = isClosed,
    createdAt = createdAt,
    viewCount = viewCount
)