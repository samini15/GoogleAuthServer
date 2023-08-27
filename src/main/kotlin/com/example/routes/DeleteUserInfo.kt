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
import io.ktor.server.sessions.*
import io.ktor.util.pipeline.*
import java.lang.Exception

fun Route.deleteUserInfo(
    application: Application,
    userDataSource: UserDataSource
) {
    authenticate(Constants.AUTHENTICATION_SESSION_NAME) {
        delete(Endpoint.DeleteUserInfo.path) {
            val userSession = call.principal<UserSession>()
            if (userSession == null) {
                application.log.info("Invalid Session.")
                call.respondRedirect(Endpoint.Unauthorized.path)
            } else {
                try {
                    call.sessions.clear<UserSession>()
                    deleteUserInfo(
                        application = application,
                        userId = userSession.id,
                        userDataSource = userDataSource
                    )
                } catch (e: Exception) {
                    application.log.error("Error Deleting User Info : ${e.message}")
                    call.respondRedirect(Endpoint.Unauthorized.path)
                }
            }
        }
    }
}

private suspend fun PipelineContext<Unit, ApplicationCall>.deleteUserInfo(
    application: Application,
    userId: String,
    userDataSource: UserDataSource
) {
    val response = userDataSource.deleteUser(userId = userId)

    if (response) {
        call.respond(
            message = ApiResponse(
                success = true,
                message = "Successfully Deleted"
            ),
            status = HttpStatusCode.OK
        )
    } else {
        application.log.info("Unable to Delete User")
        call.respond(
            message = ApiResponse(
                success = false
            ),
            status = HttpStatusCode.BadRequest
        )
    }
}