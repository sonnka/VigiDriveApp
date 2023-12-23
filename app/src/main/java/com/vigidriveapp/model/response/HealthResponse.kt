package com.vigidriveapp.model.response

import com.google.gson.annotations.SerializedName


data class HealthResponse(

    @SerializedName("id")
    val id: Long?,

    @SerializedName("time")
    val time: String?,

    @SerializedName("stressLevel")
    val stressLevel: Double?,

    @SerializedName("concentrationLevel")
    val concentrationLevel: Double?,

    @SerializedName("sleepinessLevel")
    val sleepinessLevel: Double?,

    @SerializedName("generalStatus")
    val generalStatus: Double?
)