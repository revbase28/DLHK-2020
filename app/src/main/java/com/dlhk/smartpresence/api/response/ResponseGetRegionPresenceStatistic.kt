package com.dlhk.smartpresence.api.response

import com.dlhk.smartpresence.api.response.data.DataRegionPresenceStatistic

data class ResponseGetRegionPresenceStatistic(
    val `data`: List<DataRegionPresenceStatistic>,
    val errorCode: Int,
    val message: String,
    val messageCode: Int
)