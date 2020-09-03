package com.dlhk.smartpresence.ui.smart_presence.zone_performance_statistic

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dlhk.smartpresence.api.response.ResponseGetZonePerformanceOnRegion
import com.dlhk.smartpresence.repositories.StatisticRepo
import com.dlhk.smartpresence.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class ZonePerformanceStatisticViewModel(
    val statisticRepo: StatisticRepo
): ViewModel() {

    val performanceStatistic: MutableLiveData<Resource<ResponseGetZonePerformanceOnRegion>> = MutableLiveData()

    fun getZonePerformanceStatistic(regionName: String){
        viewModelScope.launch {
            performanceStatistic.postValue(Resource.Loading())
            val response = statisticRepo.getZonePerformanceStatisticOnRegion(regionName)
            handleZonePerformanceStatisticResponse(response)
        }
    }

    private fun handleZonePerformanceStatisticResponse(response: Response<ResponseGetZonePerformanceOnRegion>){
        if(response.isSuccessful){
            response.body().let { response ->
                performanceStatistic.postValue(Resource.Success(response))
            }
        }else{
            performanceStatistic.postValue(Resource.Error(response.message()))
        }
    }
}