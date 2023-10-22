package org.swm.att.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.swm.att.data.remote.datasource.MenuDataSource
import org.swm.att.data.remote.request.CategoryPostDTO
import org.swm.att.data.remote.request.NewMenuDTO
import org.swm.att.data.remote.response.RecipeDTO
import org.swm.att.data.remote.response.StockWithMixtedDTO
import org.swm.att.domain.entity.request.NewMenuVO
import org.swm.att.domain.entity.response.CategoriesVO
import org.swm.att.domain.entity.response.CategoryIdVO
import org.swm.att.domain.entity.response.MenuIdVO
import org.swm.att.domain.entity.response.MenuWithRecipeVO
import org.swm.att.domain.entity.response.OptionListVO
import org.swm.att.domain.entity.response.StockIdVO
import org.swm.att.domain.entity.response.StockWithMixedVO
import org.swm.att.domain.entity.response.StocksVO
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

    override suspend fun postCategory(storeId: Int, categoryName: String): Flow<Result<CategoryIdVO>> = flow {
        try {
            val categoryPostInfo = CategoryPostDTO(categoryName)
            menuDataSource.postCategory(storeId, categoryPostInfo).collect {
                emit(Result.success(it.toVO()))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    override suspend fun postNewMenu(storeId: Int, newMenu: NewMenuVO): Flow<Result<MenuIdVO>> = flow {
        try {
            val menuDTO = NewMenuDTO(
                name = newMenu.name,
                price = newMenu.price,
                categoryId = newMenu.categoryId,
                option = newMenu.option,
                recipe = newMenu.recipe?.map {
                    RecipeDTO(
                        id = it.id,
                        name = it.name,
                        isMixed = it.isMixed,
                        coldRegularAmount = it.coldRegularAmount?.toInt() ?: 0,
                        unit = it.unit
                    )
                }
            )
            menuDataSource.postNewMenu(storeId, menuDTO).collect {
                emit(Result.success(it.toVO()))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    override suspend fun getAllOfOption(storeId: Int): Flow<Result<OptionListVO>> = flow {
        try {
            menuDataSource.getAllOFOption(storeId).collect {
                emit(Result.success(it.toVO()))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    override suspend fun getAllOfStock(storeId: Int, name: String): Flow<Result<StocksVO>> = flow {
        try {
            menuDataSource.getAllOfStocks(storeId, name).collect {
                emit(Result.success(it.toVO()))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    override suspend fun postNewStock(storeId: Int, newStock: StockWithMixedVO): Flow<Result<StockIdVO>> = flow {
        try {
            val stockWithMixtedDTO = StockWithMixtedDTO(
                id = newStock.id,
                name = newStock.name,
                isMixed = newStock.isMixed
            )
            menuDataSource.postNewStock(storeId, stockWithMixtedDTO).collect {
                emit(Result.success(it.toVO()))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
}