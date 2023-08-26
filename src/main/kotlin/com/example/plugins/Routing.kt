package com.example.plugins

import com.example.routes.root
import io.ktor.server.application.Application
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        root()
    }
}
