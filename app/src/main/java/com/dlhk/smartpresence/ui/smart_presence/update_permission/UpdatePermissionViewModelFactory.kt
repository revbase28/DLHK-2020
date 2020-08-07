package com.dlhk.smartpresence.ui.smart_presence.update_permission

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dlhk.smartpresence.repositories.AttendanceRepo
import com.dlhk.smartpresence.repositories.EmployeeRepo

class UpdatePermissionViewModelFactory(
    val attendanceRepo: AttendanceRepo,
    val employeeRepo: EmployeeRepo
) : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return UpdatePermissionViewModel(attendanceRepo, employeeRepo) as T
    }
}