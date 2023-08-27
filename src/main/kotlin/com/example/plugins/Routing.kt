package com.example.plugins

import com.example.domain.repository.UserDataSource
import com.example.routes.*
import io.ktor.server.application.Application
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {
    val userDataSource: UserDataSource by inject<UserDataSource>()
    routing {
        root()
        tokenVerification(application, userDataSource)

        // CRUD
        getUserInfo(application, userDataSource)
        updateUserInfo(application, userDataSource)
        deleteUserInfo(application, userDataSource)

        signOut()
        authorized()
        unauthorized()
    }
}
