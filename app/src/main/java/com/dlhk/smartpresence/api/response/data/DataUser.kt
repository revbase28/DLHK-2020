package com.dlhk.smartpresence.api.response.data

data class DataUser(
    val Name: String,
    val Photo: String,
    val RegionName: String?,
    val RoleName: String,
    val ZoneName: String?,
    val UserId: String,
    val Shift: String
)