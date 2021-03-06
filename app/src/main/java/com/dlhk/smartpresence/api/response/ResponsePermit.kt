package com.dlhk.smartpresence.api.response

import com.dlhk.smartpresence.api.response.data.DataPermit

data class ResponsePermit(
    val data: DataPermit,
    val errorCode: Int,
    val message: String,
    val messageCode: Int
)