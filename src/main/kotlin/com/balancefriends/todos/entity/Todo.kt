package com.balancefriends.todos.entity

import com.balancefriends.todos.dto.TodoFull
import com.balancefriends.todos.dto.TodoPart
import java.time.LocalDateTime
import javax.persistence.*

@Table(name = "todo")
@Entity
class Todo(
    var name: String,
    var completed: Boolean?,
) : BaseEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
    private var completedAt: LocalDateTime? = if (completed == true) {
        LocalDateTime.now()
    } else {
        null
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Todo

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }

    fun toTodoFull(): TodoFull = TodoFull(
        id,
        name,
        completed,
        completedAt,
        createdAt,
        updatedAt
    )

    fun toTodoPart(): TodoPart = TodoPart(
        id,
        name,
        completed
    )

    fun update(name: String, completed: Boolean?) {
        this.name = name
        this.completed = completed
        if (completed == true) {
            this.completedAt = LocalDateTime.now()
        }
    }
}
