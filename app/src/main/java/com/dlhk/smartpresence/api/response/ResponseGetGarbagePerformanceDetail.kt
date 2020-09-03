package com.dlhk.smartpresence.api.response

import com.dlhk.smartpresence.api.response.data.DataGarbageDetailPerformance

data class ResponseGetGarbagePerformanceDetail(
    val `data`: DataGarbageDetailPerformance,
    val errorCode: Int,
    val message: String,
    val messageCode: Int
)