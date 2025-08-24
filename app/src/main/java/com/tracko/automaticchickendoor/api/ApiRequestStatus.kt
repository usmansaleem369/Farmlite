package com.tracko.automaticchickendoor.api

sealed class ApiRequestStatus {
    object Loading : ApiRequestStatus()
    object Idle : ApiRequestStatus()
    data class Error(val message: String) : ApiRequestStatus()
}
