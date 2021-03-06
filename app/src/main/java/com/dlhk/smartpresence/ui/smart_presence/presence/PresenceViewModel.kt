package com.dlhk.smartpresence.ui.smart_presence.presence

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dlhk.smartpresence.LocationLiveData
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

class PresenceViewModel(
    val attendanceRepo: AttendanceRepo,
    val employeeRepo: EmployeeRepo,
    val app : Application
) : AndroidViewModel(app){

    val presenceData : MutableLiveData<Resource<ResponsePresence>> = MutableLiveData()
    val employeeData : MutableLiveData<Resource<ResponseGetEmployee>> = MutableLiveData()

    private val locationData = LocationLiveData(app)

    fun createPhotoFile(context: Context): File?{
       return Utility.createPhotoFile(context)
    }

    fun decodeFile(path: String): Bitmap? {
       return Utility.decodeFile(path)
    }

    fun sendPresence(employeeId: Long, shift: String, coordinate: String, livePhoto: File){
        viewModelScope.launch {
            presenceData.postValue(Resource.Loading())
            val body = livePhoto.asRequestBody("image/*".toMediaTypeOrNull())
            val image = MultipartBody.Part.createFormData("image", livePhoto.name, body)
            val req_shift = shift.toRequestBody("text/plain".toMediaTypeOrNull());
            val req_coordinate = coordinate.toRequestBody("text/plain".toMediaTypeOrNull());

            Log.d("Shift", "${req_shift.toString()}, string = ${shift}")
            val response = attendanceRepo.presence(employeeId, req_shift, req_coordinate, image)
            presenceData.postValue(handlePresenceResponse(response))
        }
    }


    fun getCurrentLocation() = locationData

    private fun handlePresenceResponse(response: Response<ResponsePresence>): Resource<ResponsePresence>{
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

    fun getZoneLeaderPerRegion(regionName: String){
        viewModelScope.launch {
            employeeData.postValue(Resource.Loading())
            val response = employeeRepo.getZoneLeaderPerRegion(regionName)
            handleGetEmployeeResponse(response)
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