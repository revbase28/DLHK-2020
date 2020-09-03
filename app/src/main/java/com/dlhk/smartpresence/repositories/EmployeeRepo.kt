package com.dlhk.smartpresence.repositories

import com.dlhk.smartpresence.api.RetrofitInstance

class EmployeeRepo(
) {

    suspend fun getEmployeePerRegion(zoneName: String, regionName: String, shift: String) =
        RetrofitInstance.api.getEmployeePerRegion(zoneName, regionName, shift)

    suspend fun getHeadZonePerRegion(regionName: String) =
        RetrofitInstance.api.getHeadZonePerRegion(regionName)

    suspend fun getRegionCoordinator() =
        RetrofitInstance.api.getRegionCoordinator()

    suspend fun getPresencePerRegionAndRole(zoneName: String, regionName: String, role: String, shift: String) =
        RetrofitInstance.api.getPresenceDataPerRegionAndRole(zoneName, regionName, role, shift)

    suspend fun getZoneLeaderPerRegion(regionName: String) =
        RetrofitInstance.api.getZoneLeaderPerRegion(regionName)

    suspend fun getPresenceHeadOfZonePerRegion(regionName: String) =
        RetrofitInstance.api.getPresenceDataHeadOfZonePerRegion(regionName)

    suspend fun getPresenceRegionCoordinator() =
        RetrofitInstance.api.getPresenceRegionCoordinator()

}