package com.dlhk.smartpresence.ui.login

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dlhk.smartpresence.api.response.ResponseClaimUserData
import com.dlhk.smartpresence.api.response.ResponseLogin
import com.dlhk.smartpresence.repositories.UserManagementRepo
import com.dlhk.smartpresence.util.Resource
import com.dlhk.smartpresence.util.SessionManager
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Response
import java.net.SocketTimeoutException

class LoginViewModel(
    val repo: UserManagementRepo
) : ViewModel() {

    val loginData: MutableLiveData<Resource<ResponseClaimUserData>> = MutableLiveData()
    lateinit var claimUserData: Response<ResponseClaimUserData>

    fun login(username: String, password: String, context: Context) {
        try{
            viewModelScope.launch {
                loginData.postValue(Resource.Loading())
                val loginResponse = repo.login(username, password)
                handleLoginResponse(loginResponse)
            }
        }catch (e: Exception){
            when(e){
                is SocketTimeoutException ->{
                    Toast.makeText(context, "Timeout", Toast.LENGTH_LONG).show()
                }
                else -> {
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    fun saveToSession(manager: SessionManager, name: String, role: String, zone: String, region: String){
        viewModelScope.launch {
            manager.apply {
                saveSessionName(name)
                saveSessionRole(role)
                saveSessionRegion(region)
                saveSessionZone(zone)
            }
        }
    }

    private suspend fun claimUserData(accessToken: String): Response<ResponseClaimUserData> {
        val job = viewModelScope.launch {
                val claimUserDataResponse = repo.claimUserData(accessToken)
                claimUserData = claimUserDataResponse
            }
        job.join()
        return claimUserData
    }

    private fun handleLoginResponse(response: Response<ResponseLogin>){
        if(response.isSuccessful){
            response.body()?.let { result ->
                viewModelScope.launch {
                    val claimUserDataResponse = async {
                        claimUserData("Bearer ${result.access_token}") // Response<ResponseClaimUserData>
                    }.await()
                    handleClaimUserResponse(claimUserDataResponse)
                }
            }
        }else{
            loginData.postValue(Resource.Error(response.message()))
        }
    }

    private fun handleClaimUserResponse(response: Response<ResponseClaimUserData>) {
        if (response.isSuccessful) {
            response.body()?.let { claimUserDataResult ->
                loginData.postValue(Resource.Success(claimUserDataResult))
            }
        }else{
            loginData.postValue(Resource.Error(response.message()))
        }
    }
}