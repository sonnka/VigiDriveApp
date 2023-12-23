package com.vigidriveapp.network

import com.vigidriveapp.model.request.DriverRequest
import com.vigidriveapp.model.request.UpdateDriverRequest
import com.vigidriveapp.model.response.AccessResponse
import com.vigidriveapp.model.response.DriverResponse
import com.vigidriveapp.model.response.HealthResponse
import com.vigidriveapp.model.response.LoginResponse
import com.vigidriveapp.model.response.SituationResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    @Headers("Content-Type: application/json")
    @POST("/login")
    fun login(@Header("Authorization") auth: String): Call<LoginResponse>

    @Headers("Content-Type: application/json")
    @POST("/register/driver")
    fun register(@Body registrationData: DriverRequest): Call<Void>

    @GET("/drivers/{driver-id}")
    fun getDriver(
        @Header("Authorization") token: String,
        @Path("driver-id") userId: Long
    ): Call<DriverResponse>

    @Headers("Content-Type: application/json")
    @PATCH("/drivers/{driver-id}")
    fun updateDriver(
        @Header("Authorization") token: String,
        @Path("driver-id") userId: Long,
        @Body driver: UpdateDriverRequest
    ): Call<Void>

    @PATCH("/drivers/{driver-id}/emergency-number/{number}")
    fun updateEmergencyContact(
        @Header("Authorization") token: String,
        @Path("driver-id") userId: Long,
        @Path("number") number: String
    ): Call<Void>

    @GET("/drivers/{driver-id}/accesses/active")
    fun getActiveAccesses(
        @Header("Authorization") token: String,
        @Path("driver-id") userId: Long
    ): Call<List<AccessResponse>>

    @GET("/drivers/{driver-id}/accesses/inactive")
    fun getInActiveAccesses(
        @Header("Authorization") token: String,
        @Path("driver-id") userId: Long
    ): Call<List<AccessResponse>>

    @GET("/drivers/{driver-id}/accesses/{access-id}")
    fun getAccess(
        @Header("Authorization") token: String,
        @Path("driver-id") userId: Long,
        @Path("access-id") accessId: Long
    ): Call<AccessResponse>

    @PATCH("/drivers/{driver-id}/accesses/{access-id}/stop")
    fun stopAccess(
        @Header("Authorization") token: String,
        @Path("driver-id") userId: Long,
        @Path("access-id") accessId: Long
    ): Call<Void>

    @POST("/drivers/{driver-id}/accesses/{access-id}")
    fun giveAccess(
        @Header("Authorization") token: String,
        @Path("driver-id") userId: Long,
        @Path("access-id") accessId: Long
    ): Call<Void>

    @GET("/drivers/{driver-id}/health-info")
    fun getHealthInfo(
        @Header("Authorization") token: String,
        @Path("driver-id") userId: Long
    ): Call<HealthResponse>

    @GET("/drivers/{driver-id}/situations/1")
    fun getSituation(
        @Header("Authorization") token: String,
        @Path("driver-id") userId: Long
    ): Call<SituationResponse>
}