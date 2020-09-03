package com.dlhk.smartpresence.api.response.data

data class DataSweeperDetailPerformance(
    val completeness: Int,
    val dicipline: Int,
    val employeeId: Int,
    val employeeName: String,
    val employeeNumber: String,
    val regionName: String,
    val road: Int,
    val roadMedian: Int,
    val roleName: String,
    val sidewalk: Int,
    val waterRope: Int,
    val zoneName: String
)