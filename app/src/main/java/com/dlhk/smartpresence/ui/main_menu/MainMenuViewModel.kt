package com.dlhk.smartpresence.ui.main_menu

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dlhk.smartpresence.LocationLiveData
import com.dlhk.smartpresence.api.response.ResponseCheckHeadZonePresence
import com.dlhk.smartpresence.api.response.ResponseGetEmployee
import com.dlhk.smartpresence.api.response.ResponsePresence
import com.dlhk.smartpresence.repositories.AttendanceRepo
import com.dlhk.smartpresence.repositories.EmployeeRepo
import com.dlhk.smartpresence.util.Resource
import com.dlhk.smartpresence.util.Utility
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import java.io.File

class MainMenuViewModel(
   val employeeRepo: EmployeeRepo,
   val attendanceRepo: AttendanceRepo,
   val app: Application
): AndroidViewModel(app) {

    val checkHeadZonePresence : MutableLiveData<Resource<ResponseCheckHeadZonePresence>> = MutableLiveData()

    fun checkHeadZonePresence(employeeId: String){
        viewModelScope.launch {
            checkHeadZonePresence.postValue(Resource.Loading())
            val response = attendanceRepo.checkHeadZonePresence(employeeId)
            handleHeadZonePresenceResponse(response)
        }
    }

    private fun handleHeadZonePresenceResponse(response: Response<ResponseCheckHeadZonePresence>){
        if(response.isSuccessful){
            response.body().let {
                checkHeadZonePresence.postValue(Resource.Success(it))
            }
        }else{
            checkHeadZonePresence.postValue(Resource.Error(response.message()))
        }
    }

}