package com.dlhk.smartpresence.api.response.data

data class DataAssessmentSweeper(
    val completeness: Int,
    val dicipline: Int,
    val presenceId: Int,
    val road: Int,
    val roadMedian: Int,
    val sidewalk: Int,
    val sweeperId: Int,
    val waterRope: Int
)