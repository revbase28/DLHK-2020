package com.dlhk.smartpresence.ui.smart_presence.field_report

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dlhk.smartpresence.repositories.StatisticRepo

class FieldReportViewModelFactory(
    val statisticRepo: StatisticRepo
): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FieldReportViewModel(statisticRepo) as T
    }

}