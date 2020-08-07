package com.dlhk.smartpresence.api.response.data

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

data class DataEmployee(
    val age: Int,
    val bank: String,
    val employeeId: Long,
    val employeeNumber: String,
    val firstContract: String,
    val lastContract: String,
    val locationContract: String,
    val name: String,
    val personId: Int,
    val region: String,
    val regionId: Int,
    val role: String,
    val roleId: Int,
    val zone: String,
    val zoneId: Int )