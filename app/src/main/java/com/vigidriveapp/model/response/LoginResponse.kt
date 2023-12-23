package com.vigidriveapp.model.response

import com.google.gson.annotations.SerializedName

data class LoginResponse (
    @SerializedName("id")
    val id: Long,

    @SerializedName("token")
    val token: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("surname")
    val surname: String,

    @SerializedName("avatar")
    val avatar: String
)