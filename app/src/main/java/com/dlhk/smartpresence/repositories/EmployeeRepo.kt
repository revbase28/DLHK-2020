package com.dlhk.smartpresence.repositories

import com.dlhk.smartpresence.api.RetrofitInstance

class EmployeeRepo(
) {

    suspend fun getEmployeePerRegion(zoneName: String, regionName: String) =
        RetrofitInstance.api.getEmployeePerRegion(zoneName, regionName)

    suspend fun getPresencePerRegionAndRole(zoneName: String, regionName: String, role: String) =
        RetrofitInstance.api.getPresenceDataPerRegionAndRole(zoneName, regionName, role)

    suspend fun getZoneLeaderPerRegion(regionName: String) =
        RetrofitInstance.api.getZoneLeaderPerRegion(regionName)

    suspend fun getPresenceHeadOfZonePerRegion(regionName: String) =
        RetrofitInstance.api.getPresenceDataHeadOfZonePerRegion(regionName)

}