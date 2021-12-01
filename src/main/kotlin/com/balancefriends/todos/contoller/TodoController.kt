package com.balancefriends.todos.contoller

import com.balancefriends.todos.annotaion.NoAuth
import com.balancefriends.todos.dto.TodoFull
import com.balancefriends.todos.dto.TodoPart
import com.balancefriends.todos.dto.TodoRequestBody
import com.balancefriends.todos.service.TodoService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("todos")
class TodoController(
    private val service: TodoService
) {

    @NoAuth
    @GetMapping("{todoId}")
    fun getTodo(
        @PathVariable todoId: Long
    ): ResponseEntity<TodoFull> {
        return ResponseEntity.ok(service.getTodo(todoId))
    }

    @NoAuth
    @GetMapping
    fun getTodos(
        @RequestParam(required = false) limit: Int?,
        @RequestParam(required = false) skip: Int?,
    ): ResponseEntity<List<TodoPart>>{
        return ResponseEntity.ok(service.getTodos(limit, skip))
    }

    @PostMapping
    fun addTodo(
        @RequestBody body: TodoRequestBody
    ): ResponseEntity<TodoFull> {
        return ResponseEntity.ok(service.addTodo(body.name, body.completed))
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("{todoId}")
    fun deleteTodo(
        @PathVariable todoId: Long
    ) {
        service.deleteTodo(todoId)
    }

    @PutMapping("{todoId}")
    fun updateTodo(
        @PathVariable todoId: Long,
        @RequestBody body: TodoRequestBody
    ) {
        service.updateTodo(todoId, body.name, body.completed)
    }
}
