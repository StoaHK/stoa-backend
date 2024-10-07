package com.example.stoa.model

import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "forum_threads")
data class ForumThread(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    val id: Long,  // Changed from String to Long

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @Column(name = "title", length = 100, nullable = false)
    val title: String,

    @Column(name = "content", length = 1000, nullable = false)
    val content: String,

    @Column(name = "is_sticky", nullable = false)
    val isSticky: Boolean = false,

    @Column(name = "created_at", nullable = false)
    val createdAt: Instant = Instant.now(),

    @Column(name = "view_count", nullable = false)
    val viewCount: Int = 0,

    @Column(name = "is_closed", nullable = false)
    val isClosed: Boolean = false
)