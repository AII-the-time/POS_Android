package org.swm.att.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.swm.att.data.remote.datasource.MenuDataSource
import org.swm.att.data.remote.request.CategoryPostDTO
import org.swm.att.domain.entity.response.CategoriesVO
import org.swm.att.domain.entity.response.MenuWithRecipeVO
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

    override suspend fun getMenuInfo(storeId: Int, menuId: Int): Flow<Result<MenuWithRecipeVO>> =
        flow {
            try {
                menuDataSource.getMenuInfo(storeId, menuId).collect {
                    emit(Result.success(it.toVO()))
                }
            } catch (e: Exception) {
                emit(Result.failure(e))
            }
        }

    override suspend fun postCategory(storeId: Int, categoryName: String): Flow<Result<Unit>> = flow {
        try {
            val categoryPostInfo = CategoryPostDTO(categoryName)
            menuDataSource.postCategory(storeId, categoryPostInfo).collect {
                emit(Result.success(it))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
}