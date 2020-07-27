package com.dlhk.smartpresence.api.response

import com.dlhk.smartpresence.api.response.data.DataUser

data class ResponseClaimUserData(
    val data: DataUser,
    val errorCode: Int,
    val message: String,
    val messageCode: Int
)