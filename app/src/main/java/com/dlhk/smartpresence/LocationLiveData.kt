package com.dlhk.smartpresence

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.util.Log
import androidx.lifecycle.LiveData
import com.dlhk.smartpresence.api.response.data.DataLocation
import com.dlhk.smartpresence.util.Constant.Companion.FASTEST_LOCATION_INTERVAL
import com.dlhk.smartpresence.util.Constant.Companion.LOCATION_UPDATE_INTERVAL
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices

class LocationLiveData(context: Context) : LiveData<DataLocation>() {

    init {
        Log.d("Location Live Data", "Called")
    }

    private var fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    private val locationCallback = object : LocationCallback(){
        override fun onLocationResult(locationResult: LocationResult?) {
            locationResult ?: return
            for (location in locationResult.locations) {
                setLocationData(location)
            }
        }
    }

    companion object{
        val locationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = LOCATION_UPDATE_INTERVAL
            fastestInterval = FASTEST_LOCATION_INTERVAL
        }
    }

    private fun setLocationData(location: Location){
        value = DataLocation(
            longitude = location.longitude,
            latitude =  location.latitude
        )
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates(){
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            null)
    }

    @SuppressLint("MissingPermission")
    override fun onActive() {
        super.onActive()
        Log.d("Location Live Data", "Active")
        fusedLocationClient.lastLocation.addOnSuccessListener {location ->
            location?.also {
                setLocationData(it)
            }
        }
        startLocationUpdates()
    }

    override fun onInactive() {
        super.onInactive()
    }
}