package com.dlhk.smartpresence.api.response

import com.dlhk.smartpresence.api.response.data.DataIndividualPresenceStatistic

data class ResponseGetIndividualPresenceStatistic(
    val `data`: List<DataIndividualPresenceStatistic>,
    val errorCode: Int,
    val message: String,
    val messageCode: Int
)