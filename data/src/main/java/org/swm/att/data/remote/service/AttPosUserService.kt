package org.swm.att.data.remote.service

import org.swm.att.data.remote.request.PhoneNumDTO
import org.swm.att.data.remote.response.MileageDTO
import org.swm.att.data.remote.response.MileageIdDTO
import org.swm.att.data.remote.response.TokenDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Query

interface AttPosUserService {

    @POST("user/refresh")
    suspend fun refreshToken(
        @Header("Authorization") refreshToken: String
    ): Response<TokenDTO>

    @GET("mileage")
    suspend fun getMileage(
        @Header("StoreId") storeId: Int,
        @Query("phone") phoneNumber: String
    ): Response<MileageDTO>

    @PATCH("mileage")
    suspend fun patchMileage(
        @Header("StoreId") storeId: Int,
        @Body mileage: MileageDTO
    ): Response<MileageDTO>

    @POST("mileage")
    suspend fun registerCustomer(
        @Header("StoreId") storeId: Int,
        @Body phone: PhoneNumDTO
    ): Response<MileageIdDTO>

}