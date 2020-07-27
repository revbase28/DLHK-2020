package com.dlhk.smartpresence.ui.smart_presence.presence

import android.Manifest
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dlhk.smartpresence.LocationLiveData
import com.dlhk.smartpresence.api.response.ResponsePresence
import com.dlhk.smartpresence.repositories.AttendanceRepo
import com.dlhk.smartpresence.util.Resource
import com.dlhk.smartpresence.util.Utility
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class PresenceViewModel(
    val repo: AttendanceRepo,
    val app : Application
) : AndroidViewModel(app){

    val presenceData : MutableLiveData<Resource<ResponsePresence>> = MutableLiveData()
    lateinit var presenceResponse: Response<ResponsePresence>
    private val locationData = LocationLiveData(app)

    fun createPhotoFile(context: Context): File?{
       return Utility.createPhotoFile(context)
    }

    fun decodeFile(path: String): Bitmap? {
       return Utility.decodeFile(path)
    }

    fun sendPresence(employeeId: Long, dateOfPresence: String, coordinate: String, livePhoto: ByteArray){
        viewModelScope.launch {
            presenceData.postValue(Resource.Loading())
            val response = repo.presence(employeeId, dateOfPresence, coordinate, livePhoto)
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

}