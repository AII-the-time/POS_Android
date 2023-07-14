package org.swm.att.domain.repository

import org.swm.att.domain.entity.response.PongVO

interface PingRepository {
    suspend fun getPong(): Result<PongVO>
}