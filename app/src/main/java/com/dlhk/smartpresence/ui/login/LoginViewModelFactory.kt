package com.dlhk.smartpresence.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dlhk.smartpresence.repositories.EmployeeRepo
import com.dlhk.smartpresence.repositories.UserManagementRepo

class LoginViewModelFactory(
    val userManagementRepo: UserManagementRepo,
    val employeeRepo: EmployeeRepo
): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return LoginViewModel(userManagementRepo, employeeRepo) as T
    }

}