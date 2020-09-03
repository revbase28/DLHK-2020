package com.dlhk.smartpresence.api.response.data

data class DataDrainageDetailPerformance(
    val cleanliness: Int,
    val completeness: Int,
    val dicipline: Int,
    val employeeId: Int,
    val employeeName: String,
    val employeeNumber: String,
    val regionName: String,
    val roleName: String,
    val sediment: Int,
    val weed: Int,
    val zoneName: String
)