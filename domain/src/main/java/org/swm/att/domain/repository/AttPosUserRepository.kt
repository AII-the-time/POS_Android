package org.swm.att.domain.repository

import kotlinx.coroutines.flow.Flow
import org.swm.att.domain.entity.request.CertificationVO
import org.swm.att.domain.entity.request.LoginVO
import org.swm.att.domain.entity.request.PhoneNumVO
import org.swm.att.domain.entity.request.StoreVO
import org.swm.att.domain.entity.response.CertificatedPhoneTokenVO
import org.swm.att.domain.entity.response.MileageIdVO
import org.swm.att.domain.entity.response.MileageVO
import org.swm.att.domain.entity.response.StoreIdVO
import org.swm.att.domain.entity.response.TokenForCertificationPhoneVO
import org.swm.att.domain.entity.response.TokenVO

interface AttPosUserRepository {
    fun refreshToken(refreshToken: String): Flow<Result<TokenVO>>
    fun saveAccessToken(accessToken: String)
    fun saveRefreshToken(refreshToken: String)
    fun getAccessToken(): String
    fun getRefreshToken(): String
    fun saveStoreId(storeId: Int)
    fun getStoreId(): Int
    suspend fun getMileage(storeId: Int, phoneNumber: String): Flow<Result<MileageVO>>
    suspend fun patchMileage(storeId: Int, mileage: MileageVO): Flow<Result<MileageVO>>
    suspend fun registerCustomer(storeId: Int, phone: PhoneNumVO): Flow<Result<MileageIdVO>>
    suspend fun registerStore(store: StoreVO): Flow<Result<StoreIdVO>>
    suspend fun registerStoreForTest(store: StoreVO): Flow<Result<StoreIdVO>>
    suspend fun postPhoneNumberForAuthentication(phone: String): Flow<Result<TokenForCertificationPhoneVO>>
    suspend fun checkCertificationCode(certificationInfo: CertificationVO): Flow<Result<CertificatedPhoneTokenVO>>
    suspend fun login(userInfo: LoginVO): Flow<Result<TokenVO>>
}