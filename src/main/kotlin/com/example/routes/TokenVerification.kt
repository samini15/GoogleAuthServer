package com.example.routes

import com.example.domain.model.ApiRequest
import com.example.domain.model.Endpoint
import com.example.domain.model.UserSession
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
import java.lang.Exception

fun Route.tokenVerification() {
    post(Endpoint.TokenVerification.path) {
        val request = call.receive<ApiRequest>()

        if (request.tokenId.isNotEmpty()) {
            val result = verifyGoogleTokenId(request.tokenId)
            if (result != null) {
                val sub = result.payload["sub"].toString()
                val name = result.payload["name"].toString()
                val email = result.payload["email"].toString()
                val profilePhoto = result.payload["picture"].toString()
                call.sessions.set(UserSession(id = "1", name = "Shayan"))
                call.respondRedirect(Endpoint.Authorized.path)
            } else {
                call.respondRedirect(Endpoint.Unauthorized.path)
            }
        } else {
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