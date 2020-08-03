package com.dlhk.smartpresence.ui.smart_presence.assesment_region_coordinator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dlhk.smartpresence.repositories.AssessmentRepo
import com.dlhk.smartpresence.repositories.EmployeeRepo

class AssessmentZoneCoordinatorViewModelFactory(
    val employeeRepo: EmployeeRepo,
    val assessmentRepo: AssessmentRepo
): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AssessmentZoneCoordinatorViewModel(employeeRepo, assessmentRepo) as T
    }
}