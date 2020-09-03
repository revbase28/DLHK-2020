package com.dlhk.smartpresence.repositories

import com.dlhk.smartpresence.api.RetrofitInstance

class StatisticRepo {

    suspend fun getRegionPresenceStatistic() = 
        RetrofitInstance.api.getRegionPresenceStatistic()

    suspend fun getRegionPerformanceStatistic() =
        RetrofitInstance.api.getRegionPerformanceStatistic()

    suspend fun getZonePresenceStatisticOnRegion(regionName: String) =
        RetrofitInstance.api.getZonePresenceStatisticOnRegion(regionName)

    suspend fun getZonePerformanceStatisticOnRegion(regionName: String) =
        RetrofitInstance.api.getZonePerformanceStatisticOnRegion(regionName)

    suspend fun getIndividualPresenceStatistic(zoneName: String, regionName: String) =
        RetrofitInstance.api.getIndividualPresenceStatistic(zoneName, regionName)

    suspend fun getIndividualPerformanceStatistic(zoneName: String) =
        RetrofitInstance.api.getIndividualPerformanceStatistic(zoneName)

    suspend fun getSweeperPerformanceDetail(id: Long) =
        RetrofitInstance.api.getSweeperPerformanceDetail(id)

    suspend fun getDrainagePerformanceDetail(id: Long) =
        RetrofitInstance.api.getDrainagePerformanceDetail(id)

    suspend fun getGarbagePerformanceDetail(id: Long) =
        RetrofitInstance.api.getGarbagePerformanceDetail(id)

    suspend fun getHeadZonePerformanceDetail(id: Long) =
        RetrofitInstance.api.getHeadZonePerformanceDetail(id)

}