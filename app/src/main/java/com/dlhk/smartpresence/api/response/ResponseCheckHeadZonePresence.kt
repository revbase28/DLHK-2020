package com.dlhk.smartpresence.api.response

data class ResponseCheckHeadZonePresence(
    val `data`: Boolean,
    val errorCode: Int,
    val message: String,
    val messageCode: Int
)