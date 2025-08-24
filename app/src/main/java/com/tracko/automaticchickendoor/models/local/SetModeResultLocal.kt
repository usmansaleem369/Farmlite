package com.tracko.automaticchickendoor.models.local

import com.tracko.automaticchickendoor.models.request.SetScheduleRequest

data class SetModeResultLocal(
    val isSuccess: Boolean,
    val data: SetScheduleRequest
)
