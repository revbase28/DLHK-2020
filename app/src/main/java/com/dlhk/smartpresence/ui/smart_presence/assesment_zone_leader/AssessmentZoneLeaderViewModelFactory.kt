package com.dlhk.smartpresence.ui.smart_presence.assesment_zone_leader

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dlhk.smartpresence.repositories.AssessmentRepo
import com.dlhk.smartpresence.repositories.EmployeeRepo

class AssessmentZoneLeaderViewModelFactory(
    val employeeRepo: EmployeeRepo,
    val assessmentRepo: AssessmentRepo,
    val app: Application
): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AssesmentZoneLeaderViewModel(app, employeeRepo, assessmentRepo) as T
    }
}