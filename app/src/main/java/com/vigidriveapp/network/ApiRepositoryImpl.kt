package com.vigidriveapp.network

import com.vigidriveapp.model.request.LoginRequest
import com.vigidriveapp.model.response.LoginResponse
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.Callback
import retrofit2.converter.gson.GsonConverterFactory

class ApiRepositoryImpl : ApiRepository {

    private val client = OkHttpClient.Builder().build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://db61-46-98-183-128.ngrok-free.app/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    private val service = retrofit.create(ApiService::class.java)

    override fun login(user: LoginRequest, callback: Callback<LoginResponse>) {
        service.login(user).enqueue(callback)
    }
}