package org.swm.att.domain.repository

import org.swm.att.domain.entity.response.TokenVO

interface AttPosUserRepository {
    suspend fun refreshToken(refreshToken: String): Result<TokenVO>
}