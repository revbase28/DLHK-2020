package com.dlhk.smartpresence.api.response

import com.dlhk.smartpresence.api.response.data.DataGetPresence

data class ResponseGetPresence(
    val `data`: List<DataGetPresence>,
    val errorCode: Int,
    val message: String,
    val messageCode: Int
)