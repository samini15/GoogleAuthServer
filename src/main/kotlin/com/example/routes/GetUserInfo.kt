package com.example.routes

import com.example.domain.model.ApiResponse
import com.example.domain.model.Endpoint
import com.example.domain.model.UserSession
import com.example.domain.repository.UserDataSource
import com.example.utils.Constants
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.lang.Exception

fun Route.getUserInfo(
    application: Application,
    userDataSource: UserDataSource
) {
    authenticate(Constants.AUTHENTICATION_SESSION_NAME) {
        get(Endpoint.GetUserInfo.path) {
            val userSession = call.principal<UserSession>()
            if (userSession == null) {
                application.log.info("Invalid Session.")
                call.respondRedirect(Endpoint.Unauthorized.path)
            } else {
                try {
                    call.respond(
                        message = ApiResponse(
                            success = true,
                            user = userDataSource.getUserInfo(userId = userSession.id)
                        ),
                        status = HttpStatusCode.OK
                    )
                } catch (e: Exception) {
                    application.log.error("Error while getting User Info: ${e.message}")
                    call.respondRedirect(Endpoint.Unauthorized.path)
                }
            }
        }
    }
}