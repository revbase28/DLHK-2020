package com.dlhk.smartpresence.api.response

data class ResponseGetEmployee(
    val `data`: List<Data>,
    val errorCode: Int,
    val message: String,
    val messageCode: Int
)