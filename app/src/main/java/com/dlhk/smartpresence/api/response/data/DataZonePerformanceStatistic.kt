package com.dlhk.smartpresence.api.response.data

data class DataZonePerformanceStatistic(
    val codeZone: String,
    val percentage: Int?,
    val regionName: String,
    val totalEmployee: Int,
    val zoneId: Int
)