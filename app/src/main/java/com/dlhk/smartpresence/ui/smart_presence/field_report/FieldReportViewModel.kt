package com.dlhk.smartpresence.ui.smart_presence.field_report

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dlhk.smartpresence.api.response.ResponseGetPresenceStatisticZoneOnRegion
import com.dlhk.smartpresence.api.response.ResponseGetRegionPerformance
import com.dlhk.smartpresence.api.response.ResponseGetRegionPresenceStatistic
import com.dlhk.smartpresence.api.response.ResponseGetZonePerformanceOnRegion
import com.dlhk.smartpresence.repositories.StatisticRepo
import com.dlhk.smartpresence.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class FieldReportViewModel(
    val statisticRepo: StatisticRepo
): ViewModel() {

    val zonePresenceStatisticData: MutableLiveData<Resource<ResponseGetPresenceStatisticZoneOnRegion>> = MutableLiveData()
    val regionPresenceStatisticData: MutableLiveData<Resource<ResponseGetRegionPresenceStatistic>> = MutableLiveData()
    val regionPerformanceStatisticData: MutableLiveData<Resource<ResponseGetRegionPerformance>> = MutableLiveData()
    val zonePerformanceStatisticData: MutableLiveData<Resource<ResponseGetZonePerformanceOnRegion>> = MutableLiveData()

    fun getZonePresenceStatisticPerRegion(regionName: String){
        viewModelScope.launch {
            zonePresenceStatisticData.postValue(Resource.Loading())
            val response = statisticRepo.getZonePresenceStatisticOnRegion(regionName)
            handleZonePresenceStatisticResponse(response)
        }
    }

    fun getRegionPresenceStatistic(){
        viewModelScope.launch {
            regionPresenceStatisticData.postValue(Resource.Loading())
            val response = statisticRepo.getRegionPresenceStatistic()
            handleRegionPresenceStatisticResponse(response)
        }
    }

    fun getRegionPerformanceStatistic(){
        viewModelScope.launch {
            regionPerformanceStatisticData.postValue(Resource.Loading())
            val response = statisticRepo.getRegionPerformanceStatistic()
            handleRegionPerformanceStatisticResponse(response)
        }
    }

    fun getZonePerformanceStatistic(regionName: String){
        viewModelScope.launch {
            zonePerformanceStatisticData.postValue(Resource.Loading())
            val response = statisticRepo.getZonePerformanceStatisticOnRegion(regionName)
            handleZonePerformanceStatisticResponse(response)
        }
    }

    private fun handleZonePresenceStatisticResponse(response: Response<ResponseGetPresenceStatisticZoneOnRegion>){
        if(response.isSuccessful){
            response.body().let { response ->
                zonePresenceStatisticData.postValue(Resource.Success(response))
            }
        }else{
            zonePresenceStatisticData.postValue(Resource.Error(response.message()))
        }
    }

    private fun handleZonePerformanceStatisticResponse(response: Response<ResponseGetZonePerformanceOnRegion>){
        if(response.isSuccessful){
            response.body().let { response ->
                zonePerformanceStatisticData.postValue(Resource.Success(response))
            }
        }else{
            zonePerformanceStatisticData.postValue(Resource.Error(response.message()))
        }
    }

    private fun handleRegionPresenceStatisticResponse(response: Response<ResponseGetRegionPresenceStatistic>){
        if(response.isSuccessful){
            response.body().let { response ->
                regionPresenceStatisticData.postValue(Resource.Success(response))
            }
        }else{
            regionPresenceStatisticData.postValue(Resource.Error(response.message()))
        }
    }

    private fun handleRegionPerformanceStatisticResponse(response: Response<ResponseGetRegionPerformance>){
        if(response.isSuccessful){
            response.body().let { response ->
                regionPerformanceStatisticData.postValue(Resource.Success(response))
            }
        }else{
            regionPerformanceStatisticData.postValue(Resource.Error(response.message()))
        }
    }

}