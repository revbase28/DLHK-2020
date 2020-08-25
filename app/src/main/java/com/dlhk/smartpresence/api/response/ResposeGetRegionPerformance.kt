package com.dlhk.smartpresence.api.response

import com.dlhk.smartpresence.api.response.data.DataRegionPerformance

data class ResposeGetRegionPerformance(
    val `data`: List<DataRegionPerformance>,
    val errorCode: Int,
    val message: String,
    val messageCode: Int
)