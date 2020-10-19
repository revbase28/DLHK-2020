package com.dlhk.smartpresence.api.response.data

data class DataPresence(
    val coordinate: String,
    val dateOfPresence: String,
    val employeeId: Int,
    val employeeName: String,
    val employeeNumber: String,
    val livePhoto: Any,
    val presenceId: Long,
    val presenceStatus: String,
    val regionName: String,
    val roleName: String,
    val shift: String,
    val timeOfPresence: String,
    val zoneName: String
)