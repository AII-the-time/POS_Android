package org.swm.att.data.remote.datasource

import org.swm.att.data.remote.model.response.PongDTO
import org.swm.att.data.remote.service.PingService
import javax.inject.Inject

class PingDataSource @Inject constructor(
    private val pingService: PingService
): BaseNetworkDataSource() {

    suspend fun getPong(): PongDTO {
        return checkResponse(pingService.getPong())
    }

}