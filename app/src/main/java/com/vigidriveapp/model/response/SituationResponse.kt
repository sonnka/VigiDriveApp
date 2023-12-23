package com.vigidriveapp.model.response

import com.google.gson.annotations.SerializedName


data class SituationResponse(
    @SerializedName("id")
    val id: Long?,

    @SerializedName("start")
    val start: String?,

    @SerializedName("end")
    val end: String?,

    @SerializedName("type")
    val type: String?,

    @SerializedName("description")
    val description: String?
)