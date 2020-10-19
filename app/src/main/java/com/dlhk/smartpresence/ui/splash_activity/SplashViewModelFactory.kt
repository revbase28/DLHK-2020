package com.dlhk.smartpresence.ui.splash_activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dlhk.smartpresence.repositories.DeviceCheckRepo

class SplashViewModelFactory(
    val deviceCheckRepo: DeviceCheckRepo
): ViewModelProvider.Factory{

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SplashViewModel(
            deviceCheckRepo
        ) as T
    }

}