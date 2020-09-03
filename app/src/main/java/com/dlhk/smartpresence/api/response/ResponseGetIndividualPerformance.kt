package com.dlhk.smartpresence.api.response

import com.dlhk.smartpresence.api.response.data.DataIndividualPerformance

data class ResponseGetIndividualPerformance(
    val `data`: List<DataIndividualPerformance>,
    val errorCode: Int,
    val message: String,
    val messageCode: Int
)