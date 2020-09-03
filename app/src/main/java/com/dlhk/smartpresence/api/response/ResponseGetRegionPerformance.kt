package com.dlhk.smartpresence.api.response

import com.dlhk.smartpresence.api.response.data.DataRegionPerformanceStatistic

data class ResponseGetRegionPerformance(
    val `data`: List<DataRegionPerformanceStatistic>,
    val errorCode: Int,
    val message: String,
    val messageCode: Int
)