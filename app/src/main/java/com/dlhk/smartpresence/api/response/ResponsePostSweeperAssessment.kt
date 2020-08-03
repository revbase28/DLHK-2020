package com.dlhk.smartpresence.api.response

import com.dlhk.smartpresence.api.response.data.DataAssessmentSweeper

data class ResponsePostSweeperAssessment(
    val data: DataAssessmentSweeper,
    val errorCode: Int,
    val message: String,
    val messageCode: Int
)