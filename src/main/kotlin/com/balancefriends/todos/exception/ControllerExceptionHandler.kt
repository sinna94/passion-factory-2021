package com.balancefriends.todos.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ControllerExceptionHandler {

    @ExceptionHandler(TodoNotFoundException::class)
    fun todoNotFoundExceptionHandler(exception: TodoNotFoundException) {
        ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.message)
    }
}