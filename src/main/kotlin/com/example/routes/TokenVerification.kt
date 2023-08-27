package com.example.routes

import com.example.domain.model.ApiRequest
import com.example.domain.model.Endpoint
import com.example.domain.model.User
import com.example.domain.model.UserSession
import com.example.domain.repository.UserDataSource
import com.example.utils.Constants
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import io.ktor.util.pipeline.*
import java.lang.Exception

fun Route.tokenVerification(
    application: Application,
    userDataSource: UserDataSource
) {
    post(Endpoint.TokenVerification.path) {
        val request = call.receive<ApiRequest>()

        if (request.tokenId.isNotEmpty()) {
            val result = verifyGoogleTokenId(request.tokenId)
            if (result != null) {
                saveUserToDatabase(
                    application = application,
                    result = result,
                    userDataSource = userDataSource
                )
            } else {
                application.log.info("Token Verification Failed.")
                call.respondRedirect(Endpoint.Unauthorized.path)
            }
        } else {
            application.log.info("Empty Token ID.")
            call.respondRedirect(Endpoint.Unauthorized.path)
        }
    }
}

fun verifyGoogleTokenId(tokenId: String): GoogleIdToken? {
    return try {
        val verifier = GoogleIdTokenVerifier.Builder(NetHttpTransport(), GsonFactory())
            .setAudience(listOf(Constants.AUDIENCE))
            .setIssuer(Constants.ISSUER)
            .build()
        verifier.verify(tokenId)
    } catch (e: Exception) {
        null
    }
}

private suspend fun PipelineContext<Unit, ApplicationCall>.saveUserToDatabase(
    application: Application,
    result: GoogleIdToken,
    userDataSource: UserDataSource
) {
    val sub = result.payload["sub"].toString()
    val name = result.payload["name"].toString()
    val email = result.payload["email"].toString()
    val profilePhoto = result.payload["picture"].toString()

    val user = User(
        id = sub,
        name = name,
        email = email,
        profilePicture = profilePhoto
    )

    val response = userDataSource.saveUserInfo(user = user)

    if (response) {
        application.log.info("User successfully SAVED/RETRIEVED.")
        call.sessions.set(UserSession(id = sub, name = name))
        call.respondRedirect(Endpoint.Authorized.path)
    } else {
        application.log.info("Error Saving User")
        call.respondRedirect(Endpoint.Unauthorized.path)
    }
}