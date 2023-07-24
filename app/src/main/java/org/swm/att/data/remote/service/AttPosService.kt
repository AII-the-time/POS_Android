package org.swm.att.data.remote.service

import org.swm.att.data.remote.model.response.MenusDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface AttPosService {
    @GET("menu/{storeId}")
    suspend fun getMenu(
        @Path("storeId") storeId: Int
    ): Response<MenusDTO>
}