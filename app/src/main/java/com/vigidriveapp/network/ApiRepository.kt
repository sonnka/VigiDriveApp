package com.vigidriveapp.network

import com.vigidriveapp.model.request.DriverRequest
import com.vigidriveapp.model.request.LoginRequest
import com.vigidriveapp.model.request.UpdateDriverRequest
import com.vigidriveapp.model.response.AccessResponse
import com.vigidriveapp.model.response.DriverResponse
import com.vigidriveapp.model.response.HealthResponse
import com.vigidriveapp.model.response.LoginResponse
import com.vigidriveapp.model.response.SituationResponse
import retrofit2.Callback

interface ApiRepository {
    fun login(
        user: LoginRequest,
        callback: Callback<LoginResponse>
    )

    fun register(
        driver: DriverRequest,
        callback: Callback<Void>
    )

    fun getDriver(
        token: String,
        userId: Long,
        callback: Callback<DriverResponse>
    )

    fun updateDriver(
        token: String,
        userId: Long,
        driver: UpdateDriverRequest,
        callback: Callback<Void>
    )

    fun updateEmergencyContact(
        token: String,
        userId: Long,
        number: String,
        callback: Callback<Void>
    )

    fun getActiveAccesses(
        token: String,
        userId: Long,
        callback: Callback<List<AccessResponse>>
    )

    fun getInActiveAccesses(
        token: String,
        userId: Long,
        callback: Callback<List<AccessResponse>>
    )

    fun stopAccess(
        token: String,
        userId: Long,
        accessId: Long,
        callback: Callback<Void>
    )

    fun giveAccess(
        token: String,
        userId: Long,
        accessId: Long,
        callback: Callback<Void>
    )

    fun getHealthInfo(
        token: String,
        userId: Long,
        callback: Callback<HealthResponse>
    )

    fun getSituation(
        token: String,
        userId: Long,
        callback: Callback<SituationResponse>
    )
}