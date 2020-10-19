package com.dlhk.smartpresence.api.response

data class ResponseCheckIfImeiRegistered(
    val `data`: Any,
    val errorCode: Int,
    val message: String,
    val messageCode: Int
)