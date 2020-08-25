package com.dlhk.smartpresence.api.response.data

data class DataZonePresenceStatistic(
    val absence: Int,
    val codeZone: String,
    val leave: Int,
    val percentage: Int,
    val presence: Int,
    val regionName: String,
    val total: Int,
    val late: Int,
    val totalEmployee: Int,
    val zoneId: Int
)