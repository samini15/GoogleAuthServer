package com.example.domain.model

sealed class Endpoint(val path: String) {
    data object Root: Endpoint(path = "/")
    data object TokenVerification: Endpoint(path = "/token_verification")
    data object GetUserInfo: Endpoint(path = "/get_user")
    data object UpdateUserInfo: Endpoint(path = "/update_user")
    data object DeleteUserInfo: Endpoint(path = "/delete_user")
    data object SignOut: Endpoint(path = "/sign_out")
    data object Unauthorized: Endpoint(path = "/unauthorized")
    data object Authorized: Endpoint(path = "/authorized")
}