package org.swm.att.data.remote.service

import org.swm.att.data.remote.response.CategoriesDTO
import org.swm.att.data.remote.response.CategoryDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface AttPosService {
    @GET("menu/")
    suspend fun getMenu(
        @Header("StoreId") storeId: Int
    ): Response<CategoriesDTO>
}