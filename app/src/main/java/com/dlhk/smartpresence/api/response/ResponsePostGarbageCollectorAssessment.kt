package com.dlhk.smartpresence.api.response

import com.dlhk.smartpresence.api.response.data.DataAssessmentGarbageCollector

data class ResponsePostGarbageCollectorAssessment(
    val dataAssessmentGarbageCollector: DataAssessmentGarbageCollector,
    val errorCode: Int,
    val message: String,
    val messageCode: Int
)