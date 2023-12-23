package com.vigidriveapp.network

import com.vigidriveapp.model.request.DriverRequest
import com.vigidriveapp.model.request.LoginRequest
import com.vigidriveapp.model.response.LoginResponse
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.Callback
import retrofit2.converter.gson.GsonConverterFactory

class ApiRepositoryImpl : ApiRepository {

    private val client = OkHttpClient.Builder().build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://5227-46-98-183-128.ngrok-free.app/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    private val service = retrofit.create(ApiService::class.java)

    override fun login(user: LoginRequest, callback: Callback<LoginResponse>) {
        val credentials = user.username + ":" + user.password
        val auth =  "Basic " + android.util.Base64.encodeToString(credentials.toByteArray(),
            android.util.Base64.NO_WRAP)
        service.login(auth).enqueue(callback)
    }

    override fun register(driver: DriverRequest, callback: Callback<Void>) {
        service.register(driver).enqueue(callback)
    }
}