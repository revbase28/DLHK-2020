package com.dlhk.smartpresence.ui.smart_presence.update_permission

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dlhk.smartpresence.api.response.ResponsePermit
import com.dlhk.smartpresence.repositories.AttendanceRepo
import com.dlhk.smartpresence.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class UpdatePermissionViewModel(
    val attendanceRepo: AttendanceRepo
): ViewModel() {

    val permitData : MutableLiveData<Resource<ResponsePermit>> = MutableLiveData()

    fun sendPermit(dateOfLeave: String, desc: String, employeeId: Long){
        viewModelScope.launch {
            permitData.postValue(Resource.Loading())
            val response = attendanceRepo.sendPermit(dateOfLeave, desc, employeeId)
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
}