package org.swm.att.domain.repository

import kotlinx.coroutines.flow.Flow
import org.swm.att.domain.entity.request.PhoneNumVO
import org.swm.att.domain.entity.request.StoreVO
import org.swm.att.domain.entity.response.MileageIdVO
import org.swm.att.domain.entity.response.MileageVO
import org.swm.att.domain.entity.response.StoreIdVO
import org.swm.att.domain.entity.response.TokenForCertificationPhoneVO
import org.swm.att.domain.entity.response.TokenVO

interface AttPosUserRepository {
    suspend fun refreshToken(refreshToken: String): Flow<Result<TokenVO>>
    suspend fun saveAccessToken(accessToken: String)
    suspend fun saveRefreshToken(refreshToken: String)
    suspend fun getAccessToken(): String
    suspend fun getRefreshToken(): String
    suspend fun saveStoreId(storeId: Int)
    suspend fun getStoreId(): Int
    suspend fun getMileage(storeId: Int, phoneNumber: String): Flow<Result<MileageVO>>
    suspend fun patchMileage(storeId: Int, mileage: MileageVO): Flow<Result<MileageVO>>
    suspend fun registerCustomer(storeId: Int, phone: PhoneNumVO): Flow<Result<MileageIdVO>>
    suspend fun registerStore(store: StoreVO): Flow<Result<StoreIdVO>>
    suspend fun registerStoreForTest(store: StoreVO): Flow<Result<StoreIdVO>>
    suspend fun postPhoneNumberForAuthentication(phone: String): Flow<Result<TokenForCertificationPhoneVO>>
}