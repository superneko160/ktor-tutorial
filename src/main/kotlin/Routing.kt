package com.example

import io.ktor.http.ContentType
import io.ktor.server.application.*
import io.ktor.server.http.content.staticResources
import io.ktor.server.response.*
import io.ktor.server.routing.*
import com.example.model.*
import io.ktor.http.HttpStatusCode

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Hello, Ktor!")
        }

        get("/foo") {
            val text = "<h1>Hello, Ktor!</h1>"
            val type = ContentType.parse("text/html")
            call.respondText(text, contentType = type)
        }

        staticResources("/content", "content")

        get("/tasks") {
            val tasks = TaskRepository.allTasks()
            call.respondText(
                contentType = ContentType.parse("text/html"),
                text = tasks.tasksAsTable()
            )
        }

        get("/tasks/byPriority/{priority?}") {
            val priorityAsText = call.parameters["priority"]
            if (priorityAsText == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }

            try {
                val priority = Priority.valueOf(priorityAsText)
                val tasks = TaskRepository.tasksByPriority(priority)

                if (tasks.isEmpty()) {
                    call.respond(HttpStatusCode.NotFound)
                    return@get
                }

                call.respondText(
                    contentType = ContentType.parse("text/html"),
                    text = tasks.tasksAsTable()
                )
            } catch (e: IllegalArgumentException) {
                call.respond(HttpStatusCode.BadRequest)
            }
        }
    }
}
