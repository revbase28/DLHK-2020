package com.dlhk.smartpresence.repositories

import com.dlhk.smartpresence.api.RetrofitInstance
import retrofit2.http.Field

class AssessmentRepo {

    suspend fun postDrainageAssessment(
        presenceId: Long,
        cleanliness: Int,
        completeness: Int,
        discipline: Int,
        sediment: Int,
        weed: Int
    ) = RetrofitInstance.api.sendDrainageAssessment(presenceId, cleanliness, completeness, discipline, sediment, weed)

    suspend fun postSweeperAssessment(
        presenceId: Long,
        road: Int,
        completeness: Int,
        discipline: Int,
        sidewalk: Int,
        waterRope: Int,
        roadMedian: Int
    ) = RetrofitInstance.api.sendSweeperAssessment(presenceId, road, completeness, discipline, sidewalk, waterRope, roadMedian)

    suspend fun postGarbageCollectorAssessment(
        presenceId: Long,
        discipline: Int,
        calculation: Int,
        separation: Int,
        tps: Int,
        organic: Int,
        anorganic: Int
    ) = RetrofitInstance.api.sendGarbageCollectorAssessment(presenceId, discipline, calculation, separation, tps, organic, anorganic)

}