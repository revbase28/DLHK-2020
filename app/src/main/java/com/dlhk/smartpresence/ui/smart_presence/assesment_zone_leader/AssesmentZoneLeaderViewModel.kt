package com.dlhk.smartpresence.ui.smart_presence.assesment_zone_leader

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dlhk.smartpresence.api.response.ResponseGetPresence
import com.dlhk.smartpresence.api.response.ResponsePostGarbageCollectorAssessment
import com.dlhk.smartpresence.api.response.ResponsePostDrainageAssessment
import com.dlhk.smartpresence.api.response.ResponsePostSweeperAssessment
import com.dlhk.smartpresence.repositories.AssessmentRepo
import com.dlhk.smartpresence.repositories.EmployeeRepo
import com.dlhk.smartpresence.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class AssesmentZoneLeaderViewModel(
    val employeeRepo: EmployeeRepo,
    val assessmentRepo: AssessmentRepo
): ViewModel() {

    val presenceData: MutableLiveData<Resource<ResponseGetPresence>> = MutableLiveData()
    val drainageAssessmentData: MutableLiveData<Resource<ResponsePostDrainageAssessment>> = MutableLiveData()
    val sweeperAssessmentData: MutableLiveData<Resource<ResponsePostSweeperAssessment>> = MutableLiveData()
    val garbageCollectorDataAssessment: MutableLiveData<Resource<ResponsePostGarbageCollectorAssessment>> = MutableLiveData()

    fun getEmployeePerRegionAndRole(zoneName: String, regionName: String, role: String){
        viewModelScope.launch {
            presenceData.postValue(Resource.Loading())
            val response = employeeRepo.getPresencePerRegionAndRole(zoneName, regionName, role)
            handleEmployeeResponse(response)
        }
    }

    fun sendDrainageAssessment(presenceId: Long,
                               cleanliness: Int,
                               completeness: Int,
                               discipline: Int,
                               sediment: Int,
                               weed: Int){
        viewModelScope.launch {
            drainageAssessmentData.postValue(Resource.Loading())
            val response = assessmentRepo.postDrainageAssessment(presenceId, cleanliness, completeness, discipline, sediment, weed)
            handleDrainageAssessmentResponse(response)
        }
    }

    fun sendSweeperAssessment(presenceId: Long,
                              road: Int,
                              completeness: Int,
                              discipline: Int,
                              sidewalk: Int,
                              waterRope: Int,
                              roadMedian: Int){
        viewModelScope.launch {
            sweeperAssessmentData.postValue(Resource.Loading())
            val response = assessmentRepo.postSweeperAssessment(presenceId, road, completeness, discipline, sidewalk, waterRope, roadMedian)
            handleSweeperAssessmentResponse(response)
        }
    }

    fun sendGarbageCollectorAssessment(
        presenceId: Long,
        discipline: Int,
        calculation: Int,
        separation: Int,
        tps: Int,
        organic: Int,
        anorganic: Int
    ){
        viewModelScope.launch {
            garbageCollectorDataAssessment.postValue(Resource.Loading())
            val response = assessmentRepo.postGarbageCollectorAssessment(presenceId, discipline, calculation, separation, tps, organic, anorganic)
            handleGarbageCollectorAssessment(response)
        }
    }

    private fun handleEmployeeResponse(response: Response<ResponseGetPresence>){
        if(response.isSuccessful){
            response.body().let { response ->
                presenceData.postValue(Resource.Success(response))
            }
        }else{
            presenceData.postValue(Resource.Error(response.message()))
        }
    }

    private fun handleDrainageAssessmentResponse(response: Response<ResponsePostDrainageAssessment>){
        if(response.isSuccessful){
            response.body().let { response ->
                drainageAssessmentData.postValue(Resource.Success(response))
            }
        }else{
            drainageAssessmentData.postValue(Resource.Error(response.message()))
        }
    }

    private fun handleSweeperAssessmentResponse(response: Response<ResponsePostSweeperAssessment>){
        if(response.isSuccessful){
            response.body().let { response ->
                sweeperAssessmentData.postValue(Resource.Success(response))
            }
        }else{
            sweeperAssessmentData.postValue(Resource.Error(response.message()))
        }
    }

    private fun handleGarbageCollectorAssessment(responseAssessment: Response<ResponsePostGarbageCollectorAssessment>){
        if(responseAssessment.isSuccessful){
            responseAssessment.body().let { response ->
                garbageCollectorDataAssessment.postValue(Resource.Success(response))
            }
        }else{
            garbageCollectorDataAssessment.postValue(Resource.Error(responseAssessment.message()))
        }
    }
}