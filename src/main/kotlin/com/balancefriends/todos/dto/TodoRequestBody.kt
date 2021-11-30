package com.balancefriends.todos.dto

data class TodoRequestBody(
    val name: String,
    val completed: Boolean?,
)
