package com.dlhk.smartpresence.api.response

import com.dlhk.smartpresence.api.response.data.DataHeadZoneDetailPerformance

data class ResponseGetHeadZoneDetailPerformance(
    val `data`: DataHeadZoneDetailPerformance,
    val errorCode: Int,
    val message: String,
    val messageCode: Int
)