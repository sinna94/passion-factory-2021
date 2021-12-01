package com.balancefriends.todos.service

import com.balancefriends.todos.dto.TodoPart
import com.balancefriends.todos.entity.Todo
import com.balancefriends.todos.exception.TodoNotFoundException
import com.balancefriends.todos.repos.TodoRepos
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
internal class TodoServiceTest(
    private val service: TodoService,
    private val repos: TodoRepos
) {

    @BeforeEach
    fun setUp() {
        repos.deleteAll()
    }

    @Test
    fun getTodoTest() {
        val todo = repos.save(Todo("todo1", null))
        val todoFull = service.getTodo(todo.id ?: 0)
        assertThat(todoFull).isEqualTo(todo.toTodoFull())
    }

    @Test
    fun getNotExistTodoTest() {
        assertThrows(TodoNotFoundException::class.java) { service.getTodo(1) }
    }

    @Test
    fun addTodoTest() {
        val savedTodo = service.addTodo("todo", null)
        val todo = service.getTodo(savedTodo.id ?: 0)
        assertThat(savedTodo).isEqualTo(todo)
    }

    @Test
    fun deleteTodoTest() {
        val todoId = repos.save(Todo("todo1", null)).id ?: 0
        repos.save(Todo("todo2", null))
        repos.save(Todo("todo3", null))

        service.deleteTodo(todoId)
        val result = repos.findAll()
        assertThat(result.count()).isEqualTo(2)
    }

    @Test
    fun updateTodoTest() {
        val todoId = repos.save(Todo("todo1", null)).id ?: 0
        val result = service.updateTodo(todoId, "todo2", true)
        assertThat(result.name).isEqualTo("todo2")
        assertThat(result.completed).isTrue
        assertThat(result.completedAt).isNotNull
    }

    @Test
    fun getTodos() {
        repos.saveAll(
            listOf(
                Todo("todo1", null),
                Todo("todo2", null),
                Todo("todo3", null),
                Todo("todo4", null),
                Todo("todo5", null),
            )
        )
        val todos = service.getTodos(2, 1)
        assertThat(todos.count()).isEqualTo(2)
        val expectedNames = listOf("todo3", "todo4")
        assertThat(todos.map(TodoPart::name))
            .isEqualTo(expectedNames)
    }
}
