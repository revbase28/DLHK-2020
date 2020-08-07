package com.dlhk.smartpresence.api

import com.dlhk.smartpresence.api.response.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

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

    @GET("employee/zone/{regionName}")
    suspend fun getZoneLeaderPerRegion(
        @Path(value="regionName", encoded = true) regionName: String
    ): Response<ResponseGetEmployee>

    @GET("presence/{zoneName}/{regionName}/{role}")
    suspend fun getPresenceDataPerRegionAndRole(
        @Path(value="zoneName", encoded = true) zoneName: String,
        @Path(value="regionName", encoded = true) regionName: String,
        @Path(value="role", encoded = true) role: String
    ): Response<ResponseGetPresence>

    @GET("presence/{regionName}/headzone")
    suspend fun getPresenceDataHeadOfZonePerRegion(
        @Path(value="regionName", encoded = true) regionName: String
    ): Response<ResponseGetPresence>

    @Multipart
    @POST("presence")
    suspend fun sendPresence(
        @Part("employeeId") employeeId:Long,
        @Part("dateOfPresence") presenceDate: RequestBody,
        @Part("coordinate") coordinate: RequestBody,
        @Part livePhoto: MultipartBody.Part
    ): Response<ResponsePresence>

    @FormUrlEncoded
    @POST("leave")
    suspend fun sendPermit(
        @Field("dateOfLeave") dateOfLeave: String,
        @Field("description") desc: String = "",
        @Field("employeeId") employeeId: Long,
        @Field("leaveStatus") leaveStatus: String
    ): Response<ResponsePermit>

    @FormUrlEncoded
    @POST("drainage")
    suspend fun sendDrainageAssessment(
        @Field("presenceId") presenceId: Long,
        @Field("cleanliness") cleanliness: Int,
        @Field("completeness") completeness: Int,
        @Field("dicipline") discipline: Int,
        @Field("sediment") sediment: Int,
        @Field("weed") weed: Int
    ): Response<ResponsePostDrainageAssessment>

    @FormUrlEncoded
    @POST("sweeper")
    suspend fun sendSweeperAssessment(
        @Field("presenceId") presenceId: Long,
        @Field("road") road: Int,
        @Field("completeness") completeness: Int,
        @Field("dicipline") discipline: Int,
        @Field("sidewalk") sidewalk: Int,
        @Field("waterRope") waterRope: Int,
        @Field("roadMedian") roadMedian: Int
    ): Response<ResponsePostSweeperAssessment>

    @FormUrlEncoded
    @POST("garbage")
    suspend fun sendGarbageCollectorAssessment(
        @Field("presenceId") presenceId: Long,
        @Field("dicipline") discipline: Int,
        @Field("calculation") calculation: Int,
        @Field("separation") separation: Int,
        @Field("TPS") tps: Int,
        @Field("volumeOfOrganic") organic: Int,
        @Field("volumeOfAnorganic") anorganic: Int
    ): Response<ResponsePostGarbageCollectorAssessment>

    @FormUrlEncoded
    @POST("headzone")
    suspend fun sendHeadOfZoneAssessment(
        @Field("presenceId") presenceId: Long,
        @Field("cleanlinessOfZone") cleanliness: Int,
        @Field("completenessOfTeam") completeness: Int,
        @Field("dataOfGarbage") dataOfGarbage: Int,
        @Field("DiciplinePresence") disciplinePresence: Int,
        @Field("firstSession") reportI: Int,
        @Field("secondSession") reportII: Int,
        @Field("thirdSession") reportIII: Int,
        @Field("typeZone") typeZone: String
    ): Response<ResponsePostZoneHeadAssessment>

}