package com.dlhk.smartpresence.ui.smart_presence.assessment_civil_apparatus

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dlhk.smartpresence.api.RetrofitInstance
import com.dlhk.smartpresence.api.response.ResponseGetEmployee
import com.dlhk.smartpresence.api.response.ResponsePostRegionCoordinatorAssessment
import com.dlhk.smartpresence.repositories.AssessmentRepo
import com.dlhk.smartpresence.repositories.EmployeeRepo
import com.dlhk.smartpresence.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class CivilApparatusViewModel(
    val employeeRepo: EmployeeRepo,
    val assessmentRepo: AssessmentRepo
): ViewModel() {

    val regionCoordinatorData: MutableLiveData<Resource<ResponseGetEmployee>> = MutableLiveData()
    val regionCoordinatorAssessmentData: MutableLiveData<Resource<ResponsePostRegionCoordinatorAssessment>> = MutableLiveData()

    fun getPresenceRegionCoordinator(){
        viewModelScope.launch {
            regionCoordinatorData.postValue(Resource.Loading())
            val response = employeeRepo.getPresenceRegionCoordinator()
            handlePresenceRegionCoordinatorData(response)
        }
    }

    fun sendRegionCoordinatorAssessment(
        employeeId: Long,
        percentOfPresence: Int,
        percentOfReport: Int,
        percentOfCompletion: Int,
        percentOfSatisfaction: Int,
        cleanliness: Int,
        dataOfGarbage: Int
    ){
        viewModelScope.launch {
            regionCoordinatorAssessmentData.postValue(Resource.Loading())
            val response = assessmentRepo.sendRegionCoordinatorAssessment(employeeId, percentOfPresence, percentOfReport, percentOfCompletion, percentOfSatisfaction, cleanliness, dataOfGarbage)
            handlePresenceRegionCoordinatorAssessment(response)
        }

    }

    private fun handlePresenceRegionCoordinatorData(response: Response<ResponseGetEmployee>){
        if(response.isSuccessful){
            response.body().let {
                regionCoordinatorData.postValue(Resource.Success(it))
            }
        }else{
            regionCoordinatorData.postValue(Resource.Error(response.message()))
        }
    }

    private fun handlePresenceRegionCoordinatorAssessment(response: Response<ResponsePostRegionCoordinatorAssessment>){
        if(response.isSuccessful){
            response.body().let {
                regionCoordinatorAssessmentData.postValue(Resource.Success(it))
            }
        }else{
            regionCoordinatorAssessmentData.postValue(Resource.Error(response.message()))
        }
    }
}