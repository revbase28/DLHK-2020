package com.dlhk.smartpresence.api.response.data

data class DataGarbageDetailPerformance(
    val calculation: Int,
    val dicipline: Int,
    val employeeId: Int,
    val employeeName: String,
    val employeeNumber: String,
    val regionName: String,
    val roleName: String,
    val separation: Int,
    val tps: Int,
    val volumeAnorganic: Int,
    val volumeOrganic: Int,
    val zoneName: String
)