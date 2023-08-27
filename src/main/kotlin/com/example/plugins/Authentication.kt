package com.example.plugins

import com.example.domain.model.Endpoint
import com.example.domain.model.UserSession
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*

fun Application.configureAuth() {
    install(Authentication) {
        session<UserSession>(name = "auth_session") {
            validate { session ->
                session
            }

            // Not authenticated successfully
            challenge {
                call.respondRedirect(Endpoint.Unauthorized.path)
            }
        }
    }
}