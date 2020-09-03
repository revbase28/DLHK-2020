package com.dlhk.smartpresence.api.response

import com.dlhk.smartpresence.api.response.data.DataAssessmentRegionCoordinator

data class ResponsePostRegionCoordinatorAssessment(
    val `data`: DataAssessmentRegionCoordinator,
    val errorCode: Int,
    val message: String,
    val messageCode: Int
)