package org.swm.att.domain.repository

import kotlinx.coroutines.flow.Flow
import org.swm.att.domain.entity.request.NewMenuVO
import org.swm.att.domain.entity.response.CategoriesVO
import org.swm.att.domain.entity.response.MenuWithRecipeVO


interface AttMenuRepository {
    suspend fun getMenu(storeId: Int): Flow<Result<CategoriesVO>>
    suspend fun getMenuInfo(storeId: Int, menuId: Int): Flow<Result<MenuWithRecipeVO>>
    suspend fun postCategory(storeId: Int, categoryName: String): Flow<Result<Unit>>
    suspend fun postNewMenu(storeId: Int, newMenu: NewMenuVO): Flow<Result<Unit>>
}