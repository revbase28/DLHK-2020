package com.dlhk.smartpresence.ui.smart_presence.individual_presence_statistic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dlhk.smartpresence.repositories.StatisticRepo

class IndividualPresenceStatisticViewModelFactory(
    val statisticRepo: StatisticRepo
): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return IndividualPresenceStatisticViewModel(statisticRepo) as T
    }

}