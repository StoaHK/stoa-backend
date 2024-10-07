package com.example.stoa.dto.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class CreateThreadRequest(
    @field:NotBlank(message = "{thread.title.notblank}")
    @field:Size(min = 3, max = 100, message = "{thread.title.size}")
    val title: String,

    @field:NotBlank(message = "{thread.content.notblank}")
    @field:Size(min = 10, max = 1000, message = "{thread.content.size}")
    val content: String
)

data class UpdateThreadRequest(
    @field:NotBlank(message = "{thread.title.notblank}")
    @field:Size(min = 3, max = 100, message = "{thread.title.size}")
    val title: String,

    @field:NotBlank(message = "{thread.content.notblank}")
    @field:Size(min = 10, max = 1000, message = "{thread.content.size}")
    val content: String,

    val isClosed: Boolean = false
)