package com.tracko.automaticchickendoor.models.remote

import com.google.gson.annotations.SerializedName

data class SetScheduleResponse(
    @SerializedName("statusCode") val statusCode: Int,
    @SerializedName("body") val body: String
)
