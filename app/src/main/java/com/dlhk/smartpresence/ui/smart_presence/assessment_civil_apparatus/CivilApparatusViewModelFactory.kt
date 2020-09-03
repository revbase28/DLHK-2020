package com.dlhk.smartpresence.ui.smart_presence.assessment_civil_apparatus

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dlhk.smartpresence.repositories.AssessmentRepo
import com.dlhk.smartpresence.repositories.EmployeeRepo
import com.dlhk.smartpresence.repositories.StatisticRepo

class CivilApparatusViewModelFactory(
    val assessmentRepo: AssessmentRepo,
    val employeeRepo: EmployeeRepo
): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CivilApparatusViewModel(employeeRepo, assessmentRepo) as T
    }
}