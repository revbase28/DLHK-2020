package com.dlhk.smartpresence.repositories

import com.dlhk.smartpresence.api.RetrofitInstance

class DeviceCheckRepo {

    suspend fun checkIfImeiRegistered(imei: String) =
        RetrofitInstance.api.checkIfImeiRegistered(imei)
}