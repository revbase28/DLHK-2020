package com.dlhk.smartpresence.api.response

import com.dlhk.smartpresence.api.response.data.DataAssessmentZoneHead

data class ResponsePostZoneHeadAssessment(
    val dataAssessmentZoneHead: DataAssessmentZoneHead,
    val errorCode: Int,
    val message: String,
    val messageCode: Int
)