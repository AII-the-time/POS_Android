package org.swm.att.data.repository

import org.swm.att.data.remote.datasource.AttEncryptedPrefDataSource
import org.swm.att.data.remote.datasource.UserDataSource
import org.swm.att.domain.entity.response.TokenVO
import org.swm.att.domain.repository.AttPosUserRepository
import org.swm.att.data.remote.datasource.AttEncryptedPrefDataSource.Companion.PreferenceKey
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
}