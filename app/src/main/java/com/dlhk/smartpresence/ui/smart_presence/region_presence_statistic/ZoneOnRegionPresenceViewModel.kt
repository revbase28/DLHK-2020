package com.dlhk.smartpresence.ui.smart_presence.region_presence_statistic

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dlhk.smartpresence.api.response.ResponseGetPresenceStatisticZoneOnRegion
import com.dlhk.smartpresence.repositories.StatisticRepo
import com.dlhk.smartpresence.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class ZoneOnRegionPresenceViewModel(
    val statisticRepo: StatisticRepo
): ViewModel() {

    val zonePresenceStatisticData: MutableLiveData<Resource<ResponseGetPresenceStatisticZoneOnRegion>> = MutableLiveData()

    fun getZonePresenceStatisticPerRegion(regionName: String){
        viewModelScope.launch {
            zonePresenceStatisticData.postValue(Resource.Loading())
            val response = statisticRepo.getZonePresenceStatisticOnRegion(regionName)
            handleZoneStatisticResponse(response)
        }
    }

    private fun handleZoneStatisticResponse(response: Response<ResponseGetPresenceStatisticZoneOnRegion>){
        if(response.isSuccessful){
            response.body().let { response ->
                zonePresenceStatisticData.postValue(Resource.Success(response))
            }
        }else{
            zonePresenceStatisticData.postValue(Resource.Error(response.message()))
        }
    }

}