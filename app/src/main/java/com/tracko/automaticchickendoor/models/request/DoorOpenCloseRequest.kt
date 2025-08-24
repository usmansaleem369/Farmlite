package com.tracko.automaticchickendoor.models.request

import com.google.gson.annotations.SerializedName

data class DoorOpenCloseRequest (
    @SerializedName("MacAddress") val macAddress: String,
    @SerializedName("command") val command : String,
)
