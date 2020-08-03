package com.dlhk.smartpresence.api.response

import com.dlhk.smartpresence.api.response.data.DataAssessmentDrainage

data class ResponsePostDrainageAssessment(
    val dataDrainage: DataAssessmentDrainage,
    val errorCode: Int,
    val message: String,
    val messageCode: Int
)