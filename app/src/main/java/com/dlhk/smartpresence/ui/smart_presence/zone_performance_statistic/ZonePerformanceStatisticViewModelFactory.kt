package com.dlhk.smartpresence.ui.smart_presence.zone_performance_statistic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dlhk.smartpresence.repositories.StatisticRepo

class ZonePerformanceStatisticViewModelFactory(
    val statisticRepo: StatisticRepo
): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ZonePerformanceStatisticViewModel(statisticRepo) as T
    }
}