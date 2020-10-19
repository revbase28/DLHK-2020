package com.dlhk.smartpresence.ui.smart_presence.update_permission

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dlhk.smartpresence.api.response.ResponseGetEmployee
import com.dlhk.smartpresence.api.response.ResponsePermit
import com.dlhk.smartpresence.repositories.AttendanceRepo
import com.dlhk.smartpresence.repositories.EmployeeRepo
import com.dlhk.smartpresence.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class UpdatePermissionViewModel(
    val attendanceRepo: AttendanceRepo,
    val employeeRepo: EmployeeRepo
): ViewModel() {

    val permitData : MutableLiveData<Resource<ResponsePermit>> = MutableLiveData()
    val employeeData : MutableLiveData<Resource<ResponseGetEmployee>> = MutableLiveData()

    fun sendPermit(dateOfLeave: String, desc: String, employeeId: Long, status: String, location: String){
        viewModelScope.launch {
            permitData.postValue(Resource.Loading())
            val response = attendanceRepo.sendPermit(dateOfLeave, desc, employeeId, status, location)
            permitData.postValue(handlePermitResponse(response))
        }
    }

    private fun handlePermitResponse(response: Response<ResponsePermit>): Resource<ResponsePermit>{
        if(response.isSuccessful){
            response.body()?.let { response ->
                return Resource.Success(response)
            }
        }

        return Resource.Error(response.message())
    }

    fun getEmployeePerRegion(zoneName: String, regionName: String, shift: String){
        viewModelScope.launch {
            employeeData.postValue(Resource.Loading())
            val getEmployeeResponse = employeeRepo.getEmployeePerRegion(zoneName, regionName, shift)
            handleGetEmployeeResponse(getEmployeeResponse)
        }
    }

    fun getHeadZonePerRegion(regionName: String){
        viewModelScope.launch {
            employeeData.postValue(Resource.Loading())
            val getEmployeeResponse = employeeRepo.getHeadZonePerRegion(regionName)
            handleGetEmployeeResponse(getEmployeeResponse)
        }
    }

    fun getRegionCoordinator(){
        viewModelScope.launch {
            employeeData.postValue(Resource.Loading())
            val getEmployeeResponse = employeeRepo.getRegionCoordinator()
            handleGetEmployeeResponse(getEmployeeResponse)
        }
    }

    private fun handleGetEmployeeResponse(response: Response<ResponseGetEmployee>){
        if(response.isSuccessful){
            response.body()?.let { employeeResult ->
                employeeData.postValue(Resource.Success(employeeResult))
            }
        }else{
            employeeData.postValue(Resource.Error(response.message()))
        }
    }
}