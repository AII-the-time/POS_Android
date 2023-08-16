package org.swm.att.domain.repository

import org.swm.att.domain.entity.request.PhoneNumVO
import org.swm.att.domain.entity.response.MileageIdVO
import org.swm.att.domain.entity.response.MileageVO
import org.swm.att.domain.entity.response.TokenVO

interface AttPosUserRepository {
    suspend fun refreshToken(refreshToken: String): Result<TokenVO>
    suspend fun saveAccessToken(accessToken: String)
    suspend fun saveRefreshToken(refreshToken: String)
    suspend fun getAccessToken(): String
    suspend fun getRefreshToken(): String
    suspend fun getMileage(storeId: Int, phoneNumber: String): Result<MileageVO>
    suspend fun patchMileage(storeId: Int, mileage: MileageVO): Result<MileageVO>
    suspend fun registerCustomer(storeId: Int, phone: PhoneNumVO): Result<MileageIdVO>
}