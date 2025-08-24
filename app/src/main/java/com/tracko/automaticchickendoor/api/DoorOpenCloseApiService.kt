package com.tracko.automaticchickendoor.api

import com.tracko.automaticchickendoor.models.remote.SetScheduleResponse
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface DoorOpenCloseApiService {
    @Headers("Content-Type: application/json")
    @POST("Direct_Door_Default/UpdateModesTable")
    suspend fun doorOpenClose(@Body requestData: RequestBody): Response<SetScheduleResponse>
}
