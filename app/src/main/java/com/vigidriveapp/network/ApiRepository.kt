package com.vigidriveapp.network

import com.vigidriveapp.model.request.LoginRequest
import com.vigidriveapp.model.response.LoginResponse
import retrofit2.Callback

interface ApiRepository {
    fun login(
        user: LoginRequest,
        callback: Callback<LoginResponse>
    )
}