package com.example.routes

import com.example.domain.model.Endpoint
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.root() {
    get(Endpoint.Root.path) {
        call.respondText("Welcome to Google Auth sample API")
    }
}