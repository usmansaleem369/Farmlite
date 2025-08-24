package com.tracko.automaticchickendoor.models.local

data class TimeSchedule(
    val endTime: String,
    val selectedDays: Set<String>,
    val scheduleSwitchEnable: Boolean
)