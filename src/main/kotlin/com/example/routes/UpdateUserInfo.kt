package com.example.routes

import com.example.domain.model.ApiResponse
import com.example.domain.model.Endpoint
import com.example.domain.model.UserSession
import com.example.domain.model.UserUpdate
import com.example.domain.repository.UserDataSource
import com.example.utils.Constants
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.pipeline.*
import java.lang.Exception

fun Route.updateUserInfo(
    application: Application,
    userDataSource: UserDataSource
) {
    authenticate(Constants.AUTHENTICATION_SESSION_NAME) {
        put(Endpoint.UpdateUserInfo.path) {
            val userSession = call.principal<UserSession>()
            val userUpdate = call.receive<UserUpdate>()
            if (userSession == null) {
                application.log.info("Invalid Session.")
                call.respondRedirect(Endpoint.Unauthorized.path)
            } else {
                try {
                    updateUserInfo(
                        application = application,
                        userId = userSession.id,
                        userUpdate = userUpdate,
                        userDataSource = userDataSource
                    )
                } catch (e: Exception) {
                    application.log.error("Error Updating User Info : ${e.message}")
                    call.respondRedirect(Endpoint.Unauthorized.path)
                }
            }
        }
    }
}

private suspend fun PipelineContext<Unit, ApplicationCall>.updateUserInfo(
    application: Application,
    userId: String,
    userUpdate: UserUpdate,
    userDataSource: UserDataSource
) {
    val response = userDataSource.updateUserInfo(
        userId = userId,
        firstName = userUpdate.firstName,
        lastName = userUpdate.lastName
    )

    if (response) {
        call.respond(
            message = ApiResponse(
                success = true,
                message = "Successfully Updated"
            ),
            status = HttpStatusCode.OK
        )
    } else {
        application.log.info("Unable to update User")
        call.respond(
            message = ApiResponse(
                success = false
            ),
            status = HttpStatusCode.BadRequest
        )
    }
}