package com.dlhk.smartpresence.ui.smart_presence.detail_performance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dlhk.smartpresence.repositories.StatisticRepo

class PerformanceDetailViewModelFactory(
    val statisticRepo: StatisticRepo
): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return PerformanceDetailViewModel(statisticRepo) as T
    }
}