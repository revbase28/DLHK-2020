package com.dlhk.smartpresence.repositories

import com.dlhk.smartpresence.api.RetrofitInstance

class AttendanceRepo {

    suspend fun presence(employeeId: Long, dateOfPresence: String, coordinate: String, livePhoto: ByteArray) =
        RetrofitInstance.api.sendPresence(employeeId, dateOfPresence, coordinate, livePhoto)


}