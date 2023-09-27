package org.swm.att.domain.repository

import kotlinx.coroutines.flow.Flow
import org.swm.att.domain.entity.response.CategoriesVO
import org.swm.att.domain.entity.response.MenuVO


interface AttMenuRepository {
    suspend fun getMenu(storeId: Int): Flow<Result<CategoriesVO>>
    suspend fun getMenuInfo(storeId: Int, menuId: Int): Flow<Result<MenuVO>>
}