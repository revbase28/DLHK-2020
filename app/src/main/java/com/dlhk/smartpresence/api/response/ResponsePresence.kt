package com.dlhk.smartpresence.api.response

import com.dlhk.smartpresence.api.response.data.DataPresence

data class ResponsePresence(
    val data: DataPresence,
    val errorCode: Int,
    val message: String,
    val messageCode: Int
)