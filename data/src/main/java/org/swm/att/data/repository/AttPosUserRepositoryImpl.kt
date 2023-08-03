package org.swm.att.data.repository

import org.swm.att.data.remote.datasource.UserDataSource
import org.swm.att.domain.entity.response.TokenVO
import org.swm.att.domain.repository.AttPosUserRepository
import javax.inject.Inject

class AttPosUserRepositoryImpl @Inject constructor(
    private val userDataSource: UserDataSource
): AttPosUserRepository {
    override suspend fun refreshToken(refreshToken: String): Result<TokenVO> {
        val response = userDataSource.refreshToken(refreshToken)
        return Result.success(response.toVO())
    }
}