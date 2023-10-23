package org.swm.att.domain.repository

import kotlinx.coroutines.flow.Flow
import org.swm.att.domain.entity.request.NewMenuVO
import org.swm.att.domain.entity.response.CategoriesVO
import org.swm.att.domain.entity.response.CategoryIdVO
import org.swm.att.domain.entity.response.MenuIdVO
import org.swm.att.domain.entity.response.MenuWithRecipeVO
import org.swm.att.domain.entity.response.OptionListVO
import org.swm.att.domain.entity.response.StockIdVO
import org.swm.att.domain.entity.response.StockVO
import org.swm.att.domain.entity.response.StockWithMixedVO
import org.swm.att.domain.entity.response.StockWithMixedListVO
import org.swm.att.domain.entity.response.StockWithStateListVO

interface AttMenuRepository {
    suspend fun getMenu(storeId: Int): Flow<Result<CategoriesVO>>
    suspend fun getMenuInfo(storeId: Int, menuId: Int): Flow<Result<MenuWithRecipeVO>>
    suspend fun postCategory(storeId: Int, categoryName: String): Flow<Result<CategoryIdVO>>
    suspend fun postNewMenu(storeId: Int, newMenu: NewMenuVO): Flow<Result<MenuIdVO>>
    suspend fun getAllOfOption(storeId: Int): Flow<Result<OptionListVO>>
    suspend fun getAllOfStock(storeId: Int, name: String): Flow<Result<StockWithMixedListVO>>
    suspend fun postNewStock(storeId: Int, newStock: StockWithMixedVO): Flow<Result<StockIdVO>>
    suspend fun getStockWithStateList(storeId: Int): Flow<Result<StockWithStateListVO>>
    suspend fun getStockById(storeId: Int, stockId: Int): Flow<Result<StockVO>>
    suspend fun postNewStock(storeId: Int, newStock: StockVO): Flow<Result<StockIdVO>>
    suspend fun updateStock(storeId: Int, stock: StockVO): Flow<Result<StockIdVO>>
}