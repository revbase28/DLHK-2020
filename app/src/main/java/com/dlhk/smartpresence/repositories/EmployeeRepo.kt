package com.dlhk.smartpresence.repositories

import com.dlhk.smartpresence.api.RetrofitInstance

class EmployeeRepo(
) {

    suspend fun getEmployeePerRegion(zoneName: String, regionName: String) =
        RetrofitInstance.api.getEmployeePerRegion(zoneName, regionName)

}