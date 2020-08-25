package com.dlhk.smartpresence.api.response.data

data class DataRegionPerformance(
    val employeTotal: Int,
    val percentage: Int,
    val regionId: Int,
    val regionName: String,
    val zoneTotal: Int
)