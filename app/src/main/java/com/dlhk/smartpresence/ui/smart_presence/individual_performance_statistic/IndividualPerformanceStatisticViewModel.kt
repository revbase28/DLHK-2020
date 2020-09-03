package com.dlhk.smartpresence.ui.smart_presence.individual_performance_statistic

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dlhk.smartpresence.api.response.ResponseGetIndividualPerformance
import com.dlhk.smartpresence.repositories.StatisticRepo
import com.dlhk.smartpresence.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class IndividualPerformanceStatisticViewModel(
    val statisticRepo: StatisticRepo
): ViewModel() {

    val individualPerformanceStatisticData: MutableLiveData<Resource<ResponseGetIndividualPerformance>> = MutableLiveData()

    fun getIndividualPerformance(zoneName: String){
        viewModelScope.launch {
            individualPerformanceStatisticData.postValue(Resource.Loading())
            val response = statisticRepo.getIndividualPerformanceStatistic(zoneName)
            handleIndividualPerformanceResponse(response)
        }
    }

    private fun handleIndividualPerformanceResponse(response: Response<ResponseGetIndividualPerformance>){
        if(response.isSuccessful){
            response.body().let {
                individualPerformanceStatisticData.postValue(Resource.Success(it))
            }
        }else{
            individualPerformanceStatisticData.postValue(Resource.Error(response.message()))
        }
    }
}