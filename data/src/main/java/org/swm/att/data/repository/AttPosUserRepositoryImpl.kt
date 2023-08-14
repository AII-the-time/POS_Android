package org.swm.att.data.repository

import org.swm.att.data.remote.datasource.AttEncryptedPrefDataSource
import org.swm.att.data.remote.datasource.UserDataSource
import org.swm.att.domain.entity.response.TokenVO
import org.swm.att.domain.repository.AttPosUserRepository
import org.swm.att.data.remote.datasource.AttEncryptedPrefDataSource.Companion.PreferenceKey
import org.swm.att.data.remote.response.MileageDTO
import org.swm.att.domain.entity.response.MileageVO
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
        val response = userDataSource.getMileage(storeId, phoneNumber)
        return Result.success(response.toVO())
    }

    override suspend fun patchMileage(storeId: Int, mileage: MileageVO): Result<MileageVO> {
        val response = userDataSource.patchMileage(
            storeId,
            MileageDTO(
                mileageId = mileage.mileageId,
                mileage = mileage.mileage
            )
        )
        return Result.success(response.toVO())
    }
}