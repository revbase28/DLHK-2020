package com.dlhk.smartpresence.ui.smart_presence.individual_presence_statistic

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dlhk.smartpresence.api.response.ResponseGetIndividualPresenceStatistic
import com.dlhk.smartpresence.repositories.StatisticRepo
import com.dlhk.smartpresence.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class IndividualPresenceStatisticViewModel(
    val statisticRepo: StatisticRepo
): ViewModel() {

    val individualStatisticData: MutableLiveData<Resource<ResponseGetIndividualPresenceStatistic>> = MutableLiveData()

    fun getIndividualStatistic(zoneName: String, regionName: String){
        viewModelScope.launch {
            individualStatisticData.postValue(Resource.Loading())
            val response = statisticRepo.getIndividualPresenceStatistic(zoneName, regionName)
            handleIndividualStatisticResponse(response)
        }
    }

    private fun handleIndividualStatisticResponse(response: Response<ResponseGetIndividualPresenceStatistic>){
        if(response.isSuccessful){
            response.body().let {
                individualStatisticData.postValue(Resource.Success(it))
            }
        }else{
            individualStatisticData.postValue(Resource.Error(response.message()))
        }
    }
}