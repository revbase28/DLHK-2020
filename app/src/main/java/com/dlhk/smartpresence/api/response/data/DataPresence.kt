package com.dlhk.smartpresence.api.response.data

data class DataPresence(
    val coordinate: String,
    val dateOfPresence: String,
    val employeeId: Int,
    val employeeName: String,
    val employeeNumber: String,
    val livePhoto: String,
    val presenceId: Int,
    val presenceStatus: String
)