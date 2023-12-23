package com.vigidriveapp.network

import com.vigidriveapp.model.request.DriverRequest
import com.vigidriveapp.model.request.LoginRequest
import com.vigidriveapp.model.request.UpdateDriverRequest
import com.vigidriveapp.model.response.AccessResponse
import com.vigidriveapp.model.response.DriverResponse
import com.vigidriveapp.model.response.HealthResponse
import com.vigidriveapp.model.response.LoginResponse
import com.vigidriveapp.model.response.SituationResponse
import okhttp3.OkHttpClient
import retrofit2.Callback
import retrofit2.Retrofit
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
        val auth = "Basic " + android.util.Base64.encodeToString(
            credentials.toByteArray(),
            android.util.Base64.NO_WRAP
        )
        service.login(auth).enqueue(callback)
    }

    override fun register(driver: DriverRequest, callback: Callback<Void>) {
        service.register(driver).enqueue(callback)
    }

    override fun getDriver(token: String, userId: Long, callback: Callback<DriverResponse>) {
        service.getDriver(token, userId).enqueue(callback)
    }

    override fun updateDriver(
        token: String,
        userId: Long,
        driver: UpdateDriverRequest,
        callback: Callback<Void>
    ) {
        service.updateDriver(token, userId, driver).enqueue(callback)
    }

    override fun updateEmergencyContact(
        token: String,
        userId: Long,
        number: String,
        callback: Callback<Void>
    ) {
        service.updateEmergencyContact(token, userId, number).enqueue(callback)
    }

    override fun getActiveAccesses(
        token: String,
        userId: Long,
        callback: Callback<List<AccessResponse>>
    ) {
        service.getActiveAccesses(token, userId).enqueue(callback)
    }

    override fun getInActiveAccesses(
        token: String,
        userId: Long,
        callback: Callback<List<AccessResponse>>
    ) {
        service.getInActiveAccesses(token, userId).enqueue(callback)
    }

    override fun getAccess(
        token: String,
        userId: Long,
        accessId: Long,
        callback: Callback<AccessResponse>
    ) {
        service.getAccess(token, userId, accessId).enqueue(callback)
    }

    override fun stopAccess(
        token: String,
        userId: Long,
        accessId: Long,
        callback: Callback<Void>
    ) {
        service.stopAccess(token, userId, accessId).enqueue(callback)
    }

    override fun giveAccess(
        token: String,
        userId: Long,
        accessId: Long,
        callback: Callback<Void>
    ) {
        service.giveAccess(token, userId, accessId).enqueue(callback)
    }

    override fun getHealthInfo(
        token: String,
        userId: Long,
        callback: Callback<HealthResponse>
    ) {
        service.getHealthInfo(token, userId).enqueue(callback)
    }

    override fun getSituation(
        token: String,
        userId: Long,
        callback: Callback<SituationResponse>
    ) {
        service.getSituation(token, userId).enqueue(callback)
    }
}