package org.swm.att.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import org.swm.att.data.remote.datasource.MenuDataSource
import org.swm.att.data.remote.request.CategoryPostDTO
import org.swm.att.data.remote.request.NewMenuDTO
import org.swm.att.data.remote.response.RecipeDTO
import org.swm.att.data.remote.response.StockDTO
import org.swm.att.data.remote.response.StockWithMixedDTO
import org.swm.att.domain.entity.request.NewMenuVO
import org.swm.att.domain.entity.response.CategoriesVO
import org.swm.att.domain.entity.response.CategoryIdVO
import org.swm.att.domain.entity.response.MenuIdVO
import org.swm.att.domain.entity.response.MenuWithRecipeVO
import org.swm.att.domain.entity.response.OptionListVO
import org.swm.att.domain.entity.response.StockIdVO
import org.swm.att.domain.entity.response.StockVO
import org.swm.att.domain.entity.response.StockWithMixedListVO
import org.swm.att.domain.entity.response.StockWithMixedVO
import org.swm.att.domain.entity.response.StockWithStateListVO
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
            val categoryPostInfo = CategoryPostDTO(null, categoryName)
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
                id = newMenu.id,
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

    override suspend fun getAllOfStock(storeId: Int, name: String): Flow<Result<StockWithMixedListVO>> = flow {
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
            val stockWithMixedDTO = StockWithMixedDTO(
                id = newStock.id,
                name = newStock.name,
                isMixed = newStock.isMixed
            )
            menuDataSource.postNewStock(storeId, stockWithMixedDTO).collect {
                emit(Result.success(it.toVO()))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    override suspend fun postNewStock(storeId: Int, newStock: StockVO): Flow<Result<StockIdVO>> = flow {
        try {
            val stockDTO = StockDTO(
                id = null,
                name = newStock.name,
                amount = newStock.amount,
                unit = newStock.unit,
                price = newStock.price,
                currentAmount = newStock.currentAmount,
                noticeThreshold = newStock.noticeThreshold,
                updatedAt = newStock.updatedAt,
                history = null
            )
            menuDataSource.postNewStock(storeId, stockDTO).collect {
                emit(Result.success(it.toVO()))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    override suspend fun getStockWithStateList(storeId: Int): Flow<Result<StockWithStateListVO>> = flow {
        try {
            menuDataSource.getStockWithStateList(storeId).collect {
                emit(Result.success(it.toVO()))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    override suspend fun getStockById(storeId: Int, stockId: Int): Flow<Result<StockVO>> = flow {
        try {
            menuDataSource.getStockById(storeId, stockId).collect {
                emit(Result.success(it.toVO()))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    override suspend fun updateStock(storeId: Int, stock: StockVO): Flow<Result<StockIdVO>> = flow {
        try {
            val stockDTO = StockDTO(
                id = stock.id,
                name = stock.name,
                amount = stock.amount,
                unit = stock.unit,
                price = stock.price,
                currentAmount = stock.currentAmount,
                noticeThreshold = stock.noticeThreshold,
                updatedAt = stock.updatedAt,
                history = null
            )
            menuDataSource.updateStock(storeId, stockDTO).collect {
                emit(Result.success(it.toVO()))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    override suspend fun deleteStock(storeId: Int, stockId: Int): Flow<Result<StockIdVO>> = flow {
        try {
            menuDataSource.deleteStock(storeId, stockId).collect {
                emit(Result.success(it.toVO()))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    override suspend fun deleteMenu(storeId: Int, menuId: Int): Flow<Result<MenuIdVO>> = flow {
        try {
            menuDataSource.deleteMenu(storeId, menuId).collect {
                emit(Result.success(it.toVO()))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    override suspend fun updateMenu(storeId: Int, menu: NewMenuVO): Flow<Result<MenuIdVO>> = flow {
        try {
            val menuVO = NewMenuDTO(
                id = menu.id,
                name = menu.name,
                price = menu.price,
                categoryId = menu.categoryId,
                option = menu.option,
                recipe = menu.recipe?.map {
                    RecipeDTO(
                        id = it.id,
                        name = it.name,
                        isMixed = it.isMixed,
                        coldRegularAmount = it.coldRegularAmount?.toInt() ?: 0,
                        unit = it.unit
                    )
                }
            )
            menuDataSource.updateMenu(storeId, menuVO).collect {
                emit(Result.success(it.toVO()))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    override suspend fun deleteCategory(storeId: Int, categoryId: Int): Flow<Result<CategoryIdVO>> = flow {
        try {
            menuDataSource.deleteCategory(storeId, categoryId).collect {
                emit(Result.success(it.toVO()))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    override suspend fun updateCategory(
        storeId: Int,
        categoryId: Int,
        categoryName: String
    ): Flow<Result<CategoryIdVO>> = flow {
        try {
            val categoryInfo = CategoryPostDTO(categoryId, categoryName)
            menuDataSource.updateCategory(storeId, categoryInfo).collect {
                emit(Result.success(it.toVO()))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
}