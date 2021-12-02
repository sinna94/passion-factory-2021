package com.balancefriends.todos.service

import com.balancefriends.todos.dto.TodoFull
import com.balancefriends.todos.dto.TodoPart
import com.balancefriends.todos.entity.Todo
import com.balancefriends.todos.exception.TodoNotFoundException
import com.balancefriends.todos.repos.TodoRepos
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.streams.toList

@Service
@Transactional
class TodoService(
    private val repos: TodoRepos
) {
    companion object {
        private const val TODO_NOT_FOUND_MESSAGE = "Todo Not Found"
    }

    fun getTodo(todoId: Long): TodoFull {
        return getTodoByTodoById(todoId).toTodoFull()
    }

    fun addTodo(name: String, completed: Boolean?): TodoFull {
        return repos.save(Todo(name, completed)).toTodoFull()
    }

    fun deleteTodo(todoId: Long) {
        repos.deleteById(todoId)
    }

    fun updateTodo(todoId: Long, name: String, completed: Boolean?): TodoFull {
        val todo = getTodoByTodoById(todoId)
        todo.update(name, completed)
        return todo.toTodoFull()
    }

    private fun getTodoByTodoById(todoId: Long): Todo {
        val todo = repos.findById(todoId).orElseThrow {
            throw TodoNotFoundException(TODO_NOT_FOUND_MESSAGE)
        }
        return todo
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
