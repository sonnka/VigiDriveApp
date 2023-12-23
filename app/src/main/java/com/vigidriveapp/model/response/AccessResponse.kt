package com.vigidriveapp.model.response

import com.google.gson.annotations.SerializedName


data class AccessResponse(

    @SerializedName("id")
    var id: Long?,

    @SerializedName("driverEmail")
    val driverEmail: String?,

    @SerializedName("managerEmail")
    val managerEmail: String?,

    @SerializedName("startDateOfAccess")
    val startDateOfAccess: String?,

    @SerializedName("endDateOfAccess")
    val endDateOfAccess: String?,

    @SerializedName("accessDuration")
    val accessDuration: String?
)