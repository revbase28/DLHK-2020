package com.dlhk.smartpresence.ui.smart_presence.field_report

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dlhk.smartpresence.api.response.ResponseGetPresenceStatisticZoneOnRegion
import com.dlhk.smartpresence.api.response.ResponseGetRegionPresenceStatistic
import com.dlhk.smartpresence.repositories.StatisticRepo
import com.dlhk.smartpresence.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class FieldReportViewModel(
    val statisticRepo: StatisticRepo
): ViewModel() {

    val zonePresenceStatisticData: MutableLiveData<Resource<ResponseGetPresenceStatisticZoneOnRegion>> = MutableLiveData()
    val regionPresenceStatisticData: MutableLiveData<Resource<ResponseGetRegionPresenceStatistic>> = MutableLiveData()

    fun getZonePresenceStatisticPerRegion(regionName: String){
        viewModelScope.launch {
            zonePresenceStatisticData.postValue(Resource.Loading())
            val response = statisticRepo.getZonePresenceStatisticOnRegion(regionName)
            handleZoneStatisticResponse(response)
        }
    }

    fun getRegionPresenceStatistic(){
        viewModelScope.launch {
            regionPresenceStatisticData.postValue(Resource.Loading())
            val response = statisticRepo.getRegionPresenceStatistic()
            handleRegionStatisticResponse(response)
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

    private fun handleRegionStatisticResponse(response: Response<ResponseGetRegionPresenceStatistic>){
        if(response.isSuccessful){
            response.body().let { response ->
                regionPresenceStatisticData.postValue(Resource.Success(response))
            }
        }else{
            regionPresenceStatisticData.postValue(Resource.Error(response.message()))
        }
    }

}