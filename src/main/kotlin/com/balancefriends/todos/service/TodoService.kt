package com.balancefriends.todos.service

import com.balancefriends.todos.dto.TodoFull
import com.balancefriends.todos.dto.TodoPart
import com.balancefriends.todos.entity.Todo
import com.balancefriends.todos.repos.TodoRepos
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.util.*
import kotlin.streams.toList

@Service
@Transactional
class TodoService(
    private val repos: TodoRepos
) {
    fun getTodo(todoId: Long): TodoFull {
        val todoOpt = getTodoOpt(todoId)

        return todoOpt.get().toTodoFull()
    }

    fun addTodo(name: String, completed: Boolean?): TodoFull {
        return repos.save(Todo(name, completed)).toTodoFull()
    }

    fun deleteTodo(todoId: Long) {
        repos.deleteById(todoId)
    }

    fun updateTodo(todoId: Long, name: String, completed: Boolean?): TodoFull {
        val todoOpt = getTodoOpt(todoId)

        val todo = todoOpt.get()
        todo.update(name, completed)

        return todo.toTodoFull()
    }

    private fun getTodoOpt(todoId: Long): Optional<Todo> {
        val todoOpt = repos.findById(todoId)

        if (todoOpt.isEmpty) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Todo Not Found")
        }

        return todoOpt
    }

    fun getTodos(limit: Int?, skip: Int?): List<TodoPart> {
        val maxLimit = 100
        val size = if (limit != null && limit > maxLimit) {
            maxLimit
        } else {
            limit ?: maxLimit
        }
        val zeroIndex = 0
        val pageRequest = PageRequest.of(skip ?: zeroIndex, size)
        return repos.findAll(pageRequest).get().map(Todo::toTodoPart).toList()
    }
}
