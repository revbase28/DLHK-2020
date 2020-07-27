package com.dlhk.smartpresence.api.response.data

data class DataPermit(
    val dateOfLeave: String,
    val desc: String,
    val employeeId: Int,
    val employeeNumber: String,
    val leaveId: Int,
    val leaveStatus: String,
    val locationContract: String,
    val personName: String,
    val phone: String
)