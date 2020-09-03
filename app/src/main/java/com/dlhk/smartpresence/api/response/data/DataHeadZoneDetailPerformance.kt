package com.dlhk.smartpresence.api.response.data

data class DataHeadZoneDetailPerformance(
    val cleanlinessZone: Int,
    val completenessTeam: Int,
    val dataOfGarbage: Int,
    val diciplinePresence: Int,
    val employeeId: Int,
    val employeeName: String,
    val employeeNumber: String,
    val firstSession: Int,
    val regionName: String,
    val roleName: String,
    val secondSession: Int,
    val thirdSession: Int,
    val zoneName: String
)