package com.dlhk.smartpresence.api.response.data

data class DataPresenceStatisticValue(
    val presence: Int,
    val leave: Int,
    val absence: Int,
    val total: Int,
    val percentage: Int
) {
}