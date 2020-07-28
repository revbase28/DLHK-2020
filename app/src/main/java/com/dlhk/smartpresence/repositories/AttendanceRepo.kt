package com.dlhk.smartpresence.repositories

import com.dlhk.smartpresence.api.RetrofitInstance
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AttendanceRepo {

    suspend fun presence(employeeId: Long, dateOfPresence: RequestBody, coordinate: RequestBody, livePhoto: MultipartBody.Part) =
        RetrofitInstance.api.sendPresence(employeeId, dateOfPresence, coordinate, livePhoto)


}