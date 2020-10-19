package com.dlhk.smartpresence.repositories

import com.dlhk.smartpresence.api.RetrofitInstance
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AttendanceRepo {

    suspend fun presence(employeeId: Long, shift: RequestBody, coordinate: RequestBody, livePhoto: MultipartBody.Part) =
        RetrofitInstance.api.sendPresence(employeeId, shift, coordinate, livePhoto)

    suspend fun sendPermit(dateOfLeave: String, desc: String, employeeId: Long, leaveStatus: String, location: String) =
        RetrofitInstance.api.sendPermit(dateOfLeave, desc, employeeId, leaveStatus, location)

    suspend fun checkHeadZonePresence(employeeId: String) =
        RetrofitInstance.api.getHeadZonePresence(employeeId)

}