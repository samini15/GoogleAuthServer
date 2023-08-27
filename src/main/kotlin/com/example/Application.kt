package com.example

import com.example.plugins.*
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureKoin() // Must be called first
    configureAuth() // Must be placed before Routing
    configureSerialization()
    configureMonitoring()
    //configureSecurity()
    configureRouting()
    configureSession()
}
