package org.swm.att.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.swm.att.data.remote.datasource.MenuDataSource
import org.swm.att.domain.entity.response.CategoriesVO
import org.swm.att.domain.repository.AttMenuRepository
import javax.inject.Inject

class AttMenuRepositoryImpl @Inject constructor(
    private val menuDataSource: MenuDataSource
) : AttMenuRepository {
    override suspend fun getMenu(storeId: Int): Flow<Result<CategoriesVO>> = flow {
        try {
            menuDataSource.getMenu(storeId).collect {
                emit(Result.success(it.toVO()))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
}