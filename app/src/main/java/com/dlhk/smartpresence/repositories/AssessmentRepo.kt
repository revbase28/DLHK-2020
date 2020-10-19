package com.dlhk.smartpresence.repositories

import com.dlhk.smartpresence.api.RetrofitInstance
import com.dlhk.smartpresence.api.response.ResponsePostZoneHeadAssessment
import retrofit2.Response
import retrofit2.http.Field

class AssessmentRepo {

    suspend fun postDrainageAssessment(
        presenceId: Long,
        cleanliness: Int,
        completeness: Int,
        discipline: Int,
        sediment: Int,
        weed: Int,
        location: String
    ) = RetrofitInstance.api.sendDrainageAssessment(presenceId, cleanliness, completeness, discipline, sediment, weed, location)

    suspend fun postSweeperAssessment(
        presenceId: Long,
        road: Int,
        completeness: Int,
        discipline: Int,
        sidewalk: Int,
        waterRope: Int,
        roadMedian: Int,
        location: String
    ) = RetrofitInstance.api.sendSweeperAssessment(presenceId, road, completeness, discipline, sidewalk, waterRope, roadMedian, location)

    suspend fun postGarbageCollectorAssessment(
        presenceId: Long,
        discipline: Int,
        calculation: Int,
        separation: Int,
        tps: Int,
        organic: Int,
        anorganic: Int,
        location: String
    ) = RetrofitInstance.api.sendGarbageCollectorAssessment(presenceId, discipline, calculation, separation, tps, organic, anorganic, location)

    suspend fun postZoneHeadAssessment(
        presenceId: Long,
        cleanliness: Int,
        completeness: Int,
        dataOfGarbage: Int,
        disciplinePresence: Int,
        reportI: Int,
        reportII: Int,
        reportIII: Int,
        typeZone: String
    ) = RetrofitInstance.api.sendHeadOfZoneAssessment(presenceId, cleanliness, completeness, dataOfGarbage, disciplinePresence, reportI, reportII, reportIII, typeZone)

    suspend fun sendRegionCoordinatorAssessment(
        employeeId: Long,
        percentOfPresence: Int,
        percentOfReport: Int,
        percentOfCompletion: Int,
        percentOfSatisfaction: Int,
        cleanliness: Int,
        dataOfGarbage: Int
    ) = RetrofitInstance.api.sendRegionCoordinatorAssessment(employeeId, percentOfPresence, percentOfReport, percentOfCompletion, percentOfSatisfaction, cleanliness, dataOfGarbage)
}