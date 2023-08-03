package org.swm.att.data.remote.service

import org.swm.att.data.remote.response.TokenDTO
import retrofit2.Response
import retrofit2.http.Header
import retrofit2.http.POST

interface AttPosUserService {

    @POST("user/refresh")
    suspend fun refreshToken(
        @Header("Authorization") refreshToken: String
    ): Response<TokenDTO>
}