package com.balancefriends.todos.contoller

import com.balancefriends.todos.dto.TodoRequestBody
import com.balancefriends.todos.entity.Todo
import com.balancefriends.todos.repos.TodoRepos
import io.restassured.RestAssured
import io.restassured.RestAssured.*
import io.restassured.http.ContentType
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.notNullValue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.springframework.test.context.TestConstructor

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
internal class TodoControllerTest(
    private val repos: TodoRepos
) {

    @LocalServerPort
    var port = 0

    @BeforeEach
    fun setUp() {
        if (RestAssured.port == UNDEFINED_PORT) {
            RestAssured.port = port
        }
        repos.deleteAll()
    }

    @Test
    fun `todo 조회 테스트`() {
        val todo = repos.save(Todo("todo", null))
        val todoId = todo.id ?: 0
        `when`().get("/todos/$todoId").then()
            .statusCode(HttpStatus.OK.value())
    }

    @Test
    fun `존재 하지 않는 id 로 todo 조회 테스트`() {
        `when`().get("/todos/20").then()
            .statusCode(HttpStatus.NOT_FOUND.value())
    }

    @Test
    fun `todo 추가 테스트`() {
        with().body(TodoRequestBody("todo", null))
            .header("Content-Type", "application/json")
            .header("Accept", "application/json")
            .`when`()
            .post("/todos?apiKey=123")
            .then()
            .statusCode(HttpStatus.OK.value())
    }

    @Test
    fun `todo 추가 Unauthorized 테스트`() {
        with().body(TodoRequestBody("todo", null))
            .header("Content-Type", "application/json")
            .header("Accept", "application/json")
            .`when`()
            .post("/todos")
            .then()
            .statusCode(HttpStatus.UNAUTHORIZED.value())
    }

    @Test
    fun `todo 삭제 테스트`() {
        val todo = repos.save(Todo("todo", null))
        val todoId = todo.id ?: 0
        `when`()
            .delete("/todos/${todoId}?apiKey=123")
            .then()
            .statusCode(HttpStatus.NO_CONTENT.value())
    }

    @Test
    fun `todo 삭제 Unauthorized 테스트`() {
        val todo = repos.save(Todo("todo", null))
        val todoId = todo.id ?: 0
        `when`()
            .delete("/todos/${todoId}")
            .then()
            .statusCode(HttpStatus.UNAUTHORIZED.value())
    }

    @Test
    fun `todo 수정 테스트`() {
        val todo = repos.save(Todo("todo", null))
        val todoId = todo.id ?: 0
        given().body(TodoRequestBody("todo2", true))
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .`when`()
            .put("/todos/${todoId}?apiKey=123")
            .then()
            .assertThat()
            .statusCode(HttpStatus.OK.value())
            .body("name", equalTo("todo2"))
            .body("completed", notNullValue())
    }

    @Test
    fun `존재하지 않는 todo 수정 테스트`() {
        given().body(TodoRequestBody("todo2", true))
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .`when`()
            .put("/todos/${99}?apiKey=123")
            .then()
            .assertThat()
            .statusCode(HttpStatus.NOT_FOUND.value())
    }

    @Test
    fun `todo 수정 Unauthorized 테스트`() {
        given().body(TodoRequestBody("todo2", true))
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .`when`()
            .put("/todos/${99}")
            .then()
            .assertThat()
            .statusCode(HttpStatus.UNAUTHORIZED.value())
    }
}