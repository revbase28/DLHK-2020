package com.dlhk.smartpresence.api.response

import com.dlhk.smartpresence.api.response.data.DataEmployee

data class ResponseGetEmployee(
    val data: List<DataEmployee>,
    val errorCode: Int,
    val message: String,
    val messageCode: Int
)