package com.dlhk.smartpresence.repositories

import com.dlhk.smartpresence.api.RetrofitInstance

class StatisticRepo {

    suspend fun getRegionPresenceStatistic() = 
        RetrofitInstance.api.getRegionPresenceStatistic()

    suspend fun getZonePresenceStatisticOnRegion(regionName: String) =
        RetrofitInstance.api.getZonePresenceStatisticOnRegion(regionName)

    suspend fun getIndividualPresenceStatistic(zoneName: String, regionName: String) =
        RetrofitInstance.api.getIndividualPresenceStatistic(zoneName, regionName)

}