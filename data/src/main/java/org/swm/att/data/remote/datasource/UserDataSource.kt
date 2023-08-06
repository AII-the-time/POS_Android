package org.swm.att.data.remote.datasource

import org.swm.att.data.remote.response.TokenDTO
import org.swm.att.data.remote.service.AttPosUserService
import javax.inject.Inject

class UserDataSource @Inject constructor(
    private val attPosUserService: AttPosUserService
): BaseNetworkDataSource() {

    suspend fun refreshToken(refreshToken: String): TokenDTO =
        checkResponse(attPosUserService.refreshToken(refreshToken))

}