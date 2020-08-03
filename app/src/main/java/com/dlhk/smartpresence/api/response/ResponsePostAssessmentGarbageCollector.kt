package com.dlhk.smartpresence.api.response

import com.dlhk.smartpresence.api.response.data.DataAssessmentGarbageCollector

data class ResponsePostAssessmentGarbageCollector(
    val dataAssessmentGarbageCollector: DataAssessmentGarbageCollector,
    val errorCode: Int,
    val message: String,
    val messageCode: Int
)