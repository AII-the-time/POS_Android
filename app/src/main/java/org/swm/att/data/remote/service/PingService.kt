package org.swm.att.data.remote.service

import org.swm.att.data.remote.model.response.PongDTO
import retrofit2.Response
import retrofit2.http.GET

interface PingService {
    @GET("ping")
    suspend fun getPong(): Response<PongDTO>
}