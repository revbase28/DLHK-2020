package com.dlhk.smartpresence.ui.smart_presence.detail_performance

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dlhk.smartpresence.api.response.ResponseGetDrainageDetailPerformance
import com.dlhk.smartpresence.api.response.ResponseGetGarbagePerformanceDetail
import com.dlhk.smartpresence.api.response.ResponseGetHeadZoneDetailPerformance
import com.dlhk.smartpresence.api.response.ResponseGetSweeperDetailPerformance
import com.dlhk.smartpresence.repositories.StatisticRepo
import com.dlhk.smartpresence.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class PerformanceDetailViewModel(
    val statisticRepo: StatisticRepo
): ViewModel() {

    val sweeperPerformanceDetail: MutableLiveData<Resource<ResponseGetSweeperDetailPerformance>> = MutableLiveData()
    val drainagePerformanceDetail: MutableLiveData<Resource<ResponseGetDrainageDetailPerformance>> = MutableLiveData()
    val garbagePerformanceDetail: MutableLiveData<Resource<ResponseGetGarbagePerformanceDetail>> = MutableLiveData()
    val headZonePerformanceDetail: MutableLiveData<Resource<ResponseGetHeadZoneDetailPerformance>> = MutableLiveData()

    fun getSweeperPerformanceDetail(id: Long){
        viewModelScope.launch {
            sweeperPerformanceDetail.postValue(Resource.Loading())
            val response = statisticRepo.getSweeperPerformanceDetail(id)
            handleSweeperPerformanceResponse(response)
        }
    }

    fun getDrainagePerformanceDetail(id: Long){
        viewModelScope.launch {
            drainagePerformanceDetail.postValue(Resource.Loading())
            val response = statisticRepo.getDrainagePerformanceDetail(id)
            handleDrainagePerformanceResponse(response)
        }
    }

    fun getGarbagePerformanceDetail(id: Long){
        viewModelScope.launch {
            garbagePerformanceDetail.postValue(Resource.Loading())
            val response = statisticRepo.getGarbagePerformanceDetail(id)
            handleGarbagePerformanceResponse(response)
        }
    }

    fun getHeadZonePerformanceDetail(id: Long){
        viewModelScope.launch {
            headZonePerformanceDetail.postValue(Resource.Loading())
            val response = statisticRepo.getHeadZonePerformanceDetail(id)
            handleHeadZonePerformanceResponse(response)
        }
    }

    private fun handleSweeperPerformanceResponse(response: Response<ResponseGetSweeperDetailPerformance>){
        if(response.isSuccessful){
            response.body().let {
                sweeperPerformanceDetail.postValue(Resource.Success(it))
            }
        }else{
            sweeperPerformanceDetail.postValue(Resource.Error(response.message()))
        }
    }

    private fun handleDrainagePerformanceResponse(response: Response<ResponseGetDrainageDetailPerformance>){
        if(response.isSuccessful){
            response.body().let {
                drainagePerformanceDetail.postValue(Resource.Success(it))
            }
        }else{
            drainagePerformanceDetail.postValue(Resource.Error(response.message()))
        }
    }

    private fun handleGarbagePerformanceResponse(response: Response<ResponseGetGarbagePerformanceDetail>){
        if(response.isSuccessful){
            response.body().let {
                garbagePerformanceDetail.postValue(Resource.Success(it))
            }
        }else{
            garbagePerformanceDetail.postValue(Resource.Error(response.message()))
        }
    }

    private fun handleHeadZonePerformanceResponse(response: Response<ResponseGetHeadZoneDetailPerformance>){
        if(response.isSuccessful){
            response.body().let {
                headZonePerformanceDetail.postValue(Resource.Success(it))
            }
        }else{
            headZonePerformanceDetail.postValue(Resource.Error(response.message()))
        }
    }
}