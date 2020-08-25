package com.dlhk.smartpresence.api.response.data

data class DataIndividualPresenceStatistic(
    val absence: Int,
    val employeeId: Int,
    val employeeName: String,
    val employeeNumber: String,
    val leave: Int,
    val late: Int,
    val locationContract: String,
    val presence: Int,
    val regionName: String,
    val roleName: String,
    val shift: String,
    val total: Int,
    val zoneName: String,
    val photo: String,
    val percentage: Int
)