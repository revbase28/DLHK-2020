package com.dlhk.smartpresence.api.response.data

data class DataGetPresence(
    val coordinate: String,
    val dateOfPresence: String,
    val employeeId: Long,
    val employeeName: String,
    val employeeNumber: String,
    val livePhoto: Any,
    val presenceId: Long,
    val presenceStatus: String,
    val regionName: String,
    val roleName: String,
    val zoneName: String,
    val counter: Int?
)