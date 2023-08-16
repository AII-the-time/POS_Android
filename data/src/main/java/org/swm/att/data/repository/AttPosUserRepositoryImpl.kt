package org.swm.att.data.repository

import org.swm.att.data.remote.datasource.AttEncryptedPrefDataSource
import org.swm.att.data.remote.datasource.AttEncryptedPrefDataSource.Companion.PreferenceKey
import org.swm.att.data.remote.datasource.UserDataSource
import org.swm.att.data.remote.request.PhoneNumDTO
import org.swm.att.data.remote.response.MileageDTO
import org.swm.att.domain.entity.request.PhoneNumVO
import org.swm.att.domain.entity.response.MileageIdVO
import org.swm.att.domain.entity.response.MileageVO
import org.swm.att.domain.entity.response.TokenVO
import org.swm.att.domain.repository.AttPosUserRepository
import javax.inject.Inject

class AttPosUserRepositoryImpl @Inject constructor(
    private val userDataSource: UserDataSource,
    private val attEncryptedPrefDataSource: AttEncryptedPrefDataSource
): AttPosUserRepository {
    override suspend fun refreshToken(refreshToken: String): Result<TokenVO> {
        val response = userDataSource.refreshToken(refreshToken)
        return Result.success(response.toVO())
    }

    override suspend fun saveAccessToken(accessToken: String) {
        attEncryptedPrefDataSource.setStrValue(
            preferenceKey = PreferenceKey.ACCESS_TOKEN,
            value = accessToken
        )
    }

    override suspend fun saveRefreshToken(refreshToken: String) {
        attEncryptedPrefDataSource.setStrValue(
            preferenceKey = PreferenceKey.REFRESH_TOKEN,
            value = refreshToken
        )
    }

    override suspend fun getAccessToken(): String {
        return attEncryptedPrefDataSource.getStrValue(PreferenceKey.ACCESS_TOKEN)
    }

    override suspend fun getRefreshToken(): String {
        return attEncryptedPrefDataSource.getStrValue(PreferenceKey.REFRESH_TOKEN)
    }

    override suspend fun getMileage(storeId: Int, phoneNumber: String): Result<MileageVO> {
        return try {
            val response = userDataSource.getMileage(storeId, phoneNumber)
            Result.success(response.toVO())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun patchMileage(storeId: Int, mileage: MileageVO): Result<MileageVO> {
        return try {
            val response = userDataSource.patchMileage(
                storeId,
                MileageDTO(
                    mileageId = mileage.mileageId,
                    mileage = mileage.mileage
                )
            )
            Result.success(response.toVO())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun registerCustomer(storeId: Int, phone: PhoneNumVO): Result<MileageIdVO> {
        return try {
            val response = userDataSource.registerCustomer(
                storeId,
                PhoneNumDTO(
                    phone = phone.phone
                )
            )
            Result.success(response.toVO())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}