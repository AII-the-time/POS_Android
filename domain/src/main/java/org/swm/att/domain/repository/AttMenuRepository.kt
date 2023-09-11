package org.swm.att.domain.repository

import kotlinx.coroutines.flow.Flow
import org.swm.att.domain.entity.response.CategoriesVO


interface AttMenuRepository {
    suspend fun getMenu(storeId: Int): Flow<Result<CategoriesVO>>
}