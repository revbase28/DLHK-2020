package com.dlhk.smartpresence.ui.smart_presence.assesment_region_coordinator

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dlhk.smartpresence.api.response.ResponseGetEmployee
import com.dlhk.smartpresence.repositories.AssessmentRepo
import com.dlhk.smartpresence.repositories.EmployeeRepo
import com.dlhk.smartpresence.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class AssessmentZoneCoordinatorViewModel(
    val employeeRepo: EmployeeRepo,
    val assessmentRepo: AssessmentRepo
): ViewModel() {

    val zoneLeaderData: MutableLiveData<Resource<ResponseGetEmployee>> = MutableLiveData()

    fun getZoneLeader(regionName: String){
        viewModelScope.launch {
            zoneLeaderData.postValue(Resource.Loading())
            val response = employeeRepo.getZoneLeaderPerRegion(regionName)
            handleZoneLeaderResponse(response)
        }
    }

    private fun handleZoneLeaderResponse(response: Response<ResponseGetEmployee>) {
        if(response.isSuccessful){
            response.body().let { response ->
                zoneLeaderData.postValue(Resource.Success(response))
            }
        }else{
            zoneLeaderData.postValue(Resource.Error(response.message()))
        }
    }
}