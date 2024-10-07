package com.example.stoa.repository

import com.example.stoa.model.ForumThread
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface ForumRepository : JpaRepository<ForumThread, Long> {
    @Query("SELECT t FROM ForumThread t ORDER BY t.isSticky DESC, t.createdAt DESC")
    fun findAllThreads(pageable: Pageable): Page<ForumThread>

    @Modifying
    @Query("UPDATE ForumThread t SET t.viewCount = t.viewCount + 1 WHERE t.id = :id")
    fun incrementViewCount(@Param("id") id: Long)
}