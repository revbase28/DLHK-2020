package com.dlhk.smartpresence.api.response

import com.dlhk.smartpresence.api.response.data.DataSweeperDetailPerformance

data class ResponseGetSweeperDetailPerformance(
    val `data`: DataSweeperDetailPerformance,
    val errorCode: Int,
    val message: String,
    val messageCode: Int
)