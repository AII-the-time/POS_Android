package org.swm.att.data.repository

import org.swm.att.data.remote.datasource.MenuDataSource
import org.swm.att.domain.entity.response.MenuVO
import org.swm.att.domain.repository.AttPosRepository
import javax.inject.Inject

class AttPosRepositoryImpl @Inject constructor(
    private val menuDataSource: MenuDataSource
) : AttPosRepository {
    override suspend fun getMenu(storeId: Int): Result<List<MenuVO>> {
        val response = menuDataSource.getMenu(storeId)
        return Result.success(response.toVO())
    }
}