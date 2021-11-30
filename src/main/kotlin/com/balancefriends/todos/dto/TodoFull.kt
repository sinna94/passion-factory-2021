package com.balancefriends.todos.dto

import java.time.LocalDateTime

data class TodoFull(
    val id: Long?,
    val name: String,
    val completed: Boolean?,
    val completedAt: LocalDateTime?,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?,
)
