package com.dlhk.smartpresence.api.response

import com.dlhk.smartpresence.api.response.data.DataPostponedPermit

data class ResponseGetPostponedPermit(
    val `data`: List<DataPostponedPermit>,
    val errorCode: Int,
    val message: String,
    val messageCode: Int
)