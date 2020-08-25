package com.dlhk.smartpresence.api.response

import com.dlhk.smartpresence.api.response.data.DataZonePresenceStatistic

data class ResponseGetPresenceStatisticZoneOnRegion(
    val `data`: List<DataZonePresenceStatistic>,
    val errorCode: Int,
    val message: String,
    val messageCode: Int
)