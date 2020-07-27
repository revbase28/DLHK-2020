package com.dlhk.smartpresence.repositories

import com.dlhk.smartpresence.api.RetrofitInstance
import retrofit2.Retrofit

class UserManagementRepo {
    suspend fun login(username: String, password: String) =
        RetrofitInstance.api.login(username, password)

    suspend fun claimUserData(accessToken: String) =
        RetrofitInstance.api.claimUserData(accessToken)
}