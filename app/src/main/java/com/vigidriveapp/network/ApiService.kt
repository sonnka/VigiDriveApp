package com.vigidriveapp.network

import com.vigidriveapp.model.request.LoginRequest
import com.vigidriveapp.model.response.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {

    @Headers("Content-Type: application/json")
    @POST("api/Auth/login")
    fun login(@Body loginData: LoginRequest): Call<LoginResponse>
}