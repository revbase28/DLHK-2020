package com.dlhk.smartpresence.ui.main_menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dlhk.smartpresence.repositories.EmployeeRepo

class MainMenuViewModelFactory(
    val employeeRepo: EmployeeRepo
): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainMenuViewModel(employeeRepo) as T
    }
}