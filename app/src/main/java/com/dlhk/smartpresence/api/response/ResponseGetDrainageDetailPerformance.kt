package com.dlhk.smartpresence.api.response

import com.dlhk.smartpresence.api.response.data.DataDrainageDetailPerformance

data class ResponseGetDrainageDetailPerformance(
    val `data`: DataDrainageDetailPerformance,
    val errorCode: Int,
    val message: String,
    val messageCode: Int
)