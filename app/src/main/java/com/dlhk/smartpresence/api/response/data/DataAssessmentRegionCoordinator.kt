package com.dlhk.smartpresence.api.response.data

data class DataAssessmentRegionCoordinator(
    val cleanliness: Int,
    val coordinatorId: Int,
    val dataOfGarbage: Int,
    val employeeId: Int,
    val percentOfCompletion: Int,
    val percentOfPresence: Int,
    val percentOfReport: Int,
    val percentOfSatisfaction: Int
)