package com.dlhk.smartpresence.ui.smart_presence.assesment_region_coordinator

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dlhk.smartpresence.api.response.ResponseGetPresence
import com.dlhk.smartpresence.api.response.ResponsePostZoneHeadAssessment
import com.dlhk.smartpresence.repositories.AssessmentRepo
import com.dlhk.smartpresence.repositories.EmployeeRepo
import com.dlhk.smartpresence.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class AssessmentRegionCoordinatorViewModel(
    val employeeRepo: EmployeeRepo,
    val assessmentRepo: AssessmentRepo
): ViewModel() {

    val zoneLeaderData: MutableLiveData<Resource<ResponseGetPresence>> = MutableLiveData()
    val headOfZoneAssessment: MutableLiveData<Resource<ResponsePostZoneHeadAssessment>> = MutableLiveData()

    fun getZoneLeader(regionName: String){
        viewModelScope.launch {
            zoneLeaderData.postValue(Resource.Loading())
            val response = employeeRepo.getPresenceHeadOfZonePerRegion(regionName)
            handleHeadOfZoneResponse(response)
        }
    }

    fun postHeadOfZoneAssessment(
        presenceId: Long,
        cleanliness: Int,
        completeness: Int,
        dataOfGarbage: Int,
        disciplinePresence: Int,
        reportI: Int,
        reportII: Int,
        reportIII: Int,
        typeZone: String
    ){
        viewModelScope.launch {
            headOfZoneAssessment.postValue(Resource.Loading())
            val response = assessmentRepo.postZoneHeadAssessment(
                presenceId,
                cleanliness,
                completeness,
                dataOfGarbage,
                disciplinePresence,
                reportI,
                reportII,
                reportIII,
                typeZone
            )
            handleHeadOfZoneAssessmentResponse(response)
        }
    }

    private fun handleHeadOfZoneResponse(response: Response<ResponseGetPresence>) {
        if(response.isSuccessful){
            response.body().let { response ->
                zoneLeaderData.postValue(Resource.Success(response))
            }
        }else{
            zoneLeaderData.postValue(Resource.Error(response.message()))
        }
    }

    private fun handleHeadOfZoneAssessmentResponse(response: Response<ResponsePostZoneHeadAssessment>) {
        if(response.isSuccessful){
            response.body().let { response ->
                headOfZoneAssessment.postValue(Resource.Success(response))
            }
        }else{
            headOfZoneAssessment.postValue(Resource.Error(response.message()))
        }
    }
}