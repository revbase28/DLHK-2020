package com.dlhk.smartpresence.ui.inventory.submit_equipment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dlhk.smartpresence.ui.login.LoginViewModel

class SubmitEquipmentViewModelFactory: ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SubmitEquipmentViewModel() as T
    }
}