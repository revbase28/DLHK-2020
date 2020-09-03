package com.dlhk.smartpresence.api.response

import com.dlhk.smartpresence.api.response.data.DataZonePerformanceStatistic

data class ResponseGetZonePerformanceOnRegion(
    val `data`: List<DataZonePerformanceStatistic>,
    val errorCode: Int,
    val message: String,
    val messageCode: Int
)