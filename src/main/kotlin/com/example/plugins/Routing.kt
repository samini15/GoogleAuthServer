package com.example.plugins

import com.example.routes.authorized
import com.example.routes.root
import com.example.routes.tokenVerification
import com.example.routes.unauthorized
import io.ktor.server.application.Application
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        root()
        tokenVerification()
        authorized()
        unauthorized()
    }
}
