package com.dlhk.smartpresence.ui.smart_presence.individual_performance_statistic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dlhk.smartpresence.repositories.StatisticRepo

class IndividualPerformanceStatisticViewModelFactory(
    val statisticRepo: StatisticRepo
): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return IndividualPerformanceStatisticViewModel(statisticRepo) as T
    }
}