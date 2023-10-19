package org.swm.att.domain.repository

import kotlinx.coroutines.flow.Flow
import org.swm.att.domain.entity.request.NewMenuVO
import org.swm.att.domain.entity.response.CategoriesVO
import org.swm.att.domain.entity.response.CategoryIdVO
import org.swm.att.domain.entity.response.MenuIdVO
import org.swm.att.domain.entity.response.MenuWithRecipeVO
import org.swm.att.domain.entity.response.OptionListVO
import org.swm.att.domain.entity.response.StocksVO

interface AttMenuRepository {
    suspend fun getMenu(storeId: Int): Flow<Result<CategoriesVO>>
    suspend fun getMenuInfo(storeId: Int, menuId: Int): Flow<Result<MenuWithRecipeVO>>
    suspend fun postCategory(storeId: Int, categoryName: String): Flow<Result<CategoryIdVO>>
    suspend fun postNewMenu(storeId: Int, newMenu: NewMenuVO): Flow<Result<MenuIdVO>>
    suspend fun getAllOfOption(storeId: Int): Flow<Result<OptionListVO>>
    suspend fun getAllOfStock(storeId: Int, name: String): Flow<Result<StocksVO>>
}