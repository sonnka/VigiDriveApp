package com.vigidriveapp.model.response

import com.google.gson.annotations.SerializedName


data class DriverResponse(

    @SerializedName("id")
    val id: Long?,

    @SerializedName("firstName")
    val firstName: String?,

    @SerializedName("lastName")
    val lastName: String?,

    @SerializedName("email")
    val email: String?,

    @SerializedName("dateOfBirth")
    val dateOfBirth: String?,

    @SerializedName("phoneNumber")
    val phoneNumber: String?,

    @SerializedName("emergencyContact")
    val emergencyContact: String?,

    @SerializedName("avatar")
    val avatar: String?

)