package com.balancefriends.todos

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication
@EnableJpaAuditing
class TodosApplication

fun main(args: Array<String>) {
    runApplication<TodosApplication>(*args)
}
