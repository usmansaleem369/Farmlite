package com.tracko.automaticchickendoor.models.request

import com.google.gson.annotations.SerializedName
import com.tracko.automaticchickendoor.util.DoorCommandMode

data class SetScheduleRequest(
    @SerializedName("Email") val email: String,
    @SerializedName("MacAddress") val macAddress: String,
    @SerializedName("mode") val mode: DoorCommandMode,
    @SerializedName("open_time") val openTime: String,
    @SerializedName("close_time") val closeTime: String
)
