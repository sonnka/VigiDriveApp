package com.vigidriveapp.network

import com.vigidriveapp.model.request.DriverRequest
import com.vigidriveapp.model.request.LoginRequest
import com.vigidriveapp.model.response.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {

    @Headers("Content-Type: application/json")
    @POST("/login")
    fun login(@Header("Authorization") auth: String,): Call<LoginResponse>

    @Headers("Content-Type: application/json")
    @POST("/register/driver")
    fun register(@Body registrationData: DriverRequest): Call<Void>

}