package com.dlhk.smartpresence.ui.smart_presence.zone_presence_statistic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dlhk.smartpresence.repositories.StatisticRepo

class ZoneOnRegionPresenceViewModelFactory(
    val statisticRepo: StatisticRepo
): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ZoneOnRegionPresenceViewModel(statisticRepo) as T
    }

}