package com.dlhk.smartpresence.ui.main_menu

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dlhk.smartpresence.repositories.AttendanceRepo
import com.dlhk.smartpresence.repositories.EmployeeRepo

class MainMenuViewModelFactory(
    val employeeRepo: EmployeeRepo,
    val attendanceRepo: AttendanceRepo,
    val app: Application
): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainMenuViewModel(employeeRepo, attendanceRepo , app) as T
    }
}