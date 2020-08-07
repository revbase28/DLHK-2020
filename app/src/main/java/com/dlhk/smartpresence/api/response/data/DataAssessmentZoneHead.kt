package com.dlhk.smartpresence.api.response.data

data class DataAssessmentZoneHead(
    val assessmentZoneId: Int,
    val cleanlinessZone: Int,
    val completenessTeam: Int,
    val dataOfGarbage: Int,
    val diciplinePresence: Int,
    val employeeId: Int,
    val firstSession: Int,
    val secondSession: Int,
    val thirdSession: Int,
    val typeZone: String
)