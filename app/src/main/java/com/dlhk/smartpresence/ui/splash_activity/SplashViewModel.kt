package com.dlhk.smartpresence.ui.splash_activity

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dlhk.smartpresence.api.response.ResponseCheckIfImeiRegistered
import com.dlhk.smartpresence.repositories.DeviceCheckRepo
import com.dlhk.smartpresence.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class SplashViewModel(
    val deviceCheckRepo: DeviceCheckRepo
): ViewModel() {

    val checkDeviceIdData: MutableLiveData<Resource<ResponseCheckIfImeiRegistered>> = MutableLiveData()

    fun checkIfImeiRegistered(imei: String) {
        viewModelScope.launch {
            checkDeviceIdData.postValue(Resource.Loading())
            val response = deviceCheckRepo.checkIfImeiRegistered(imei)
            handleCheckImeiResponse(response)
        }
    }

    private fun handleCheckImeiResponse(response: Response<ResponseCheckIfImeiRegistered>) {
        if (response.isSuccessful) {
            response.body().let { imeiCheckResult ->
                checkDeviceIdData.postValue(Resource.Success(imeiCheckResult))
            }
        } else {
            checkDeviceIdData.postValue(Resource.Error(response.message()))
        }
    }
}