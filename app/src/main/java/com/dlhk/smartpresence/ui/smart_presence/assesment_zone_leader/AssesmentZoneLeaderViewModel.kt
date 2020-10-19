package com.dlhk.smartpresence.ui.smart_presence.assesment_zone_leader

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dlhk.smartpresence.LocationLiveData
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
    val app: Application,
    val employeeRepo: EmployeeRepo,
    val assesmentRepo: AssessmentRepo
): AndroidViewModel(app) {

    val presenceData: MutableLiveData<Resource<ResponseGetPresence>> = MutableLiveData()
    val drainageAssessmentData: MutableLiveData<Resource<ResponsePostDrainageAssessment>> = MutableLiveData()
    val sweeperAssessmentData: MutableLiveData<Resource<ResponsePostSweeperAssessment>> = MutableLiveData()
    val garbageCollectorDataAssessment: MutableLiveData<Resource<ResponsePostGarbageCollectorAssessment>> = MutableLiveData()

    val locationData = LocationLiveData(app)

    fun getEmployeePerRegionAndRole(zoneName: String, regionName: String, role: String, shift: String){
        viewModelScope.launch {
            presenceData.postValue(Resource.Loading())
            val response = employeeRepo.getPresencePerRegionAndRole(zoneName, regionName, role, shift)
            handleEmployeeResponse(response)
        }
    }

    fun sendDrainageAssessment(presenceId: Long,
                               cleanliness: Int,
                               completeness: Int,
                               discipline: Int,
                               sediment: Int,
                               weed: Int,
                               location: String
    ) {
        viewModelScope.launch {
            drainageAssessmentData.postValue(Resource.Loading())
            val response = assesmentRepo.postDrainageAssessment(presenceId, cleanliness, completeness, discipline, sediment, weed, location)
            handleDrainageAssessmentResponse(response)
        }
    }

    fun sendSweeperAssessment(presenceId: Long,
                              road: Int,
                              completeness: Int,
                              discipline: Int,
                              sidewalk: Int,
                              waterRope: Int,
                              roadMedian: Int,
                              location: String
    ) {
        viewModelScope.launch {
            sweeperAssessmentData.postValue(Resource.Loading())
            val response = assesmentRepo.postSweeperAssessment(presenceId, road, completeness, discipline, sidewalk, waterRope, roadMedian, location)
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
        anorganic: Int,
        location: String
    ){
        viewModelScope.launch {
            garbageCollectorDataAssessment.postValue(Resource.Loading())
            val response = assesmentRepo.postGarbageCollectorAssessment(presenceId, discipline, calculation, separation, tps, organic, anorganic, location)
            handleGarbageCollectorAssessment(response)
        }
    }

    fun getCurrentLocation() = locationData

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