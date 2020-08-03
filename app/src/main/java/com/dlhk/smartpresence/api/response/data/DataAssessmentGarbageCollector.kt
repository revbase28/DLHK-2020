package com.dlhk.smartpresence.api.response.data

data class DataAssessmentGarbageCollector(
    val calculation: Int,
    val dicipline: Int,
    val garbageId: Int,
    val presenceId: Int,
    val separation: Int,
    val tps: Int,
    val volumeAnorganic: Int,
    val volumeOrganic: Int
)