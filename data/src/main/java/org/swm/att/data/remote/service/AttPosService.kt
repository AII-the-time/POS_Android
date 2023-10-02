package org.swm.att.data.remote.service

import org.swm.att.data.remote.request.OrderedMenusDTO
import org.swm.att.data.remote.request.PaymentDTO
import org.swm.att.data.remote.response.CategoriesDTO
import org.swm.att.data.remote.response.MenuWithRecipeDTO
import org.swm.att.data.remote.response.OrderBillsDTO
import org.swm.att.data.remote.response.OrderDTO
import org.swm.att.data.remote.response.OrderReceiptDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface AttPosService {
    @GET("menu/")
    suspend fun getMenu(
        @Header("StoreId") storeId: Int
    ): Response<CategoriesDTO>

    @GET("menu/{menuId}")
    suspend fun getMenuInfo(
        @Header("StoreId") storeId: Int,
        @Path("menuId") menuId: Int
    ): Response<MenuWithRecipeDTO>

    @POST("order/")
    suspend fun postOrder(
        @Header("StoreId") storeId: Int,
        @Body orderedMenus: OrderedMenusDTO
    ): Response<OrderDTO>

    @POST("order/pay")
    suspend fun postPayment(
        @Header("StoreId") storeId: Int,
        @Body payOrder: PaymentDTO
    ): Response<Unit>

    @GET("order/")
    suspend fun getOrderBills(
        @Header("storeId") storeId: Int,
        @Query("page") page: Int,
        @Query("count") count: Int
    ): Response<OrderBillsDTO>

    @GET("order/{orderId}")
    suspend fun getOrderBill(
        @Header("storeId") storeId: Int,
        @Path("orderId") orderId: Int
    ): Response<OrderReceiptDTO>
}