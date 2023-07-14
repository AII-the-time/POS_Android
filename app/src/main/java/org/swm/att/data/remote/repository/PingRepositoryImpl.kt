package org.swm.att.data.remote.repository

import org.swm.att.data.remote.datasource.PingDataSource
import org.swm.att.data.remote.service.PingService
import org.swm.att.domain.entity.response.PongVO
import org.swm.att.domain.repository.PingRepository
import javax.inject.Inject
import javax.inject.Singleton

class PingRepositoryImpl @Inject constructor(
    private val pingDataSource: PingDataSource
): PingRepository {
    override suspend fun getPong(): Result<PongVO> {
        val response = pingDataSource.getPong()
        return Result.success(response.toVO())
    }
}