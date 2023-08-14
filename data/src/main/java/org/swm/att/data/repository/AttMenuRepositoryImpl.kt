package org.swm.att.data.repository

import org.swm.att.data.remote.datasource.MenuDataSource
import org.swm.att.domain.entity.response.CategoriesVO
import org.swm.att.domain.repository.AttMenuRepository
import javax.inject.Inject

class AttMenuRepositoryImpl @Inject constructor(
    private val menuDataSource: MenuDataSource
) : AttMenuRepository {
    override suspend fun getMenu(storeId: Int): Result<CategoriesVO> {
        val response = menuDataSource.getMenu(storeId)
        return Result.success(response.toVO())
    }
}