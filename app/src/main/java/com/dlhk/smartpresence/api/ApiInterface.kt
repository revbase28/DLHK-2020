package com.dlhk.smartpresence.api

import com.dlhk.smartpresence.api.response.ResponseClaimUserData
import com.dlhk.smartpresence.api.response.ResponseGetEmployee
import com.dlhk.smartpresence.api.response.ResponseLogin
import com.dlhk.smartpresence.api.response.ResponsePresence
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*
import java.io.File

interface ApiInterface {

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("grant_type") grant_type: String = "password"): Response<ResponseLogin>

    @GET("user/claim")
    suspend fun claimUserData(
        @Header("Authorization") accessToken: String): Response<ResponseClaimUserData>

    @GET("employee/{zoneName}/{regionName}")
    suspend fun getEmployeePerRegion(
        @Path(value="zoneName", encoded = true) zoneName: String,
        @Path(value="regionName", encoded = true) regionName: String
    ): Response<ResponseGetEmployee>

    @Multipart
    @POST("presence")
    suspend fun sendPresence(
        @Part("employeeId") employeeId:Long,
        @Part("dateOfPresence") presenceDate: RequestBody,
        @Part("coordinate") coordinate: RequestBody,
        @Part livePhoto: MultipartBody.Part ): Response<ResponsePresence>
}