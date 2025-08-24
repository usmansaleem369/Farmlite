package com.tracko.automaticchickendoor.api

import com.tracko.automaticchickendoor.models.remote.SetScheduleResponse
import com.tracko.automaticchickendoor.models.request.SetScheduleRequest
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST


interface ApiService {
    @Headers("Content-Type: application/json")
    @POST("Default/")
    suspend fun setSchedule(@Body requestData: RequestBody): Response<SetScheduleResponse>

}