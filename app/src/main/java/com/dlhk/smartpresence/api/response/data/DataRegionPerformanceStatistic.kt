package com.dlhk.smartpresence.api.response.data

data class DataRegionPerformanceStatistic(
    val employeTotal: Int,
    val percentage: Int?,
    val regionId: Int,
    val regionName: String,
    val zoneTotal: Int
)