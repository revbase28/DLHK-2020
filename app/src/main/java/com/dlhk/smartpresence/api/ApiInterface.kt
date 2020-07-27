package com.dlhk.smartpresence.api

import com.dlhk.smartpresence.api.response.ResponseClaimUserData
import com.dlhk.smartpresence.api.response.ResponseLogin
import com.dlhk.smartpresence.api.response.ResponsePresence
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
    )

    @Multipart
    @POST("presence")
    suspend fun sendPresence(
        @Field("employeeId") employeeId:Long,
        @Field("dateOfPresence") presenceDate: String,
        @Field("coordinate") coordinate: String,
        @Field("livePhoto") livePhoto: ByteArray): Response<ResponsePresence>
}