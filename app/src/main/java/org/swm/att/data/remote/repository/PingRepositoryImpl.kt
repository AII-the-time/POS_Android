package org.swm.att.data.remote.repository

import org.swm.att.domain.entity.response.PongVO
import org.swm.att.domain.repository.PingRepository

class PingRepositoryImpl: PingRepository {
    override suspend fun getPong(): PongVO {
        return PongVO("test")
    }
}