package com.dlhk.smartpresence.api.response.data

data class DataRegionPresenceStatistic(
    val absence: Int,
    val employeTotal: Int,
    val leave: Int,
    val percentage: Int,
    val presence: Int,
    val regionId: Int,
    val regionName: String,
    val zoneTotal: Int
)