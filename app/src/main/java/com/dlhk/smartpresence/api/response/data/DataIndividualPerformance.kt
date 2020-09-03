package com.dlhk.smartpresence.api.response.data

data class DataIndividualPerformance(
    val employeeId: Long,
    val employeeName: String,
    val employeeNumber: String,
    val locationContract: String,
    val percentage: Int?,
    val photo: String,
    val regionName: String,
    val roleName: String,
    val shift: String,
    val zoneName: String
)