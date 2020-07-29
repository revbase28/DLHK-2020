package com.dlhk.smartpresence.ui.main_menu

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dlhk.smartpresence.api.response.ResponseGetEmployee
import com.dlhk.smartpresence.repositories.EmployeeRepo
import com.dlhk.smartpresence.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class MainMenuViewModel(
   val employeeRepo: EmployeeRepo
): ViewModel() {

    val employeeData : MutableLiveData<Resource<ResponseGetEmployee>> = MutableLiveData()

    fun getEmployeePerRegion(zoneName: String, regionName: String){
        viewModelScope.launch {
            employeeData.postValue(Resource.Loading())
            val getEmployeeResponse = employeeRepo.getEmployeePerRegion(zoneName, regionName)
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