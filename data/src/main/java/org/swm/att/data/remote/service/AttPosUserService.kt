package org.swm.att.data.remote.service

import org.swm.att.data.remote.request.CertificationDTO
import org.swm.att.data.remote.request.LoginDTO
import org.swm.att.data.remote.request.PhoneNumDTO
import org.swm.att.data.remote.request.StoreDTO
import org.swm.att.data.remote.response.CertificatedPhoneTokenDTO
import org.swm.att.data.remote.response.MileageDTO
import org.swm.att.data.remote.response.MileageIdDTO
import org.swm.att.data.remote.response.SduiBaseResponseDTO
import org.swm.att.data.remote.response.StoreIdDTO
import org.swm.att.data.remote.response.StoreListDTO
import org.swm.att.data.remote.response.TokenDTO
import org.swm.att.data.remote.response.TokenForCertificationPhoneDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Query

interface AttPosUserService {

    @GET("report")
    suspend fun getUserReport(
        @Header("StoreId") storeId: Int
    ): Response<SduiBaseResponseDTO>

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

    @POST("store")
    suspend fun registerStore(
        @Body store: StoreDTO
    ): Response<StoreIdDTO>

    @POST("store/test")
    suspend fun registerStoreForTest(
        @Body store: StoreDTO
    ): Response<StoreIdDTO>

    @POST("user/phone")
    suspend fun postPhoneNumberForAuthentication(
        @Body phone: PhoneNumDTO
    ): Response<TokenForCertificationPhoneDTO>

    @POST("user/phone/certificationCode")
    suspend fun checkCertificationCode(
       @Body certificationBody: CertificationDTO
    ): Response<CertificatedPhoneTokenDTO>

    @POST("user/login")
    suspend fun login(
        @Body userInfo: LoginDTO
    ): Response<TokenDTO>

    @GET("store")
    suspend fun getStore(): Response<StoreListDTO>

}