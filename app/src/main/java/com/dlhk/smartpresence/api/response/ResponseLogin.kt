package com.dlhk.smartpresence.api.response

data class ResponseLogin(
    val access_token: String,
    val expires_in: Int,
    val token_type: String
)