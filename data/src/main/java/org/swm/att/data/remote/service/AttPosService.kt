package org.swm.att.data.remote.service

import org.swm.att.data.remote.request.OrderedMenusDTO
import org.swm.att.data.remote.response.CategoriesDTO
import org.swm.att.data.remote.response.OrderDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface AttPosService {
    @GET("menu/")
    suspend fun getMenu(
        @Header("StoreId") storeId: Int
    ): Response<CategoriesDTO>

    @POST("order/")
    suspend fun postOrder(
        @Header("StoreId") storeId: Int,
        @Body orderedMenus: OrderedMenusDTO
    ): Response<OrderDTO>
}