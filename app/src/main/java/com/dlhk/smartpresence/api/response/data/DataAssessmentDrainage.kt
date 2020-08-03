package com.dlhk.smartpresence.api.response.data

data class DataAssessmentDrainage(
    val cleanliness: Int,
    val completeness: Int,
    val dicipline: Int,
    val drainangeId: Int,
    val presenceId: Int,
    val sediment: Int,
    val weed: Int
)