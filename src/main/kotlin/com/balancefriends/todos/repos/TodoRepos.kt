package com.balancefriends.todos.repos

import com.balancefriends.todos.entity.Todo
import org.springframework.data.jpa.repository.JpaRepository

interface TodoRepos : JpaRepository<Todo, Long>
