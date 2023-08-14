package org.swm.att.domain.repository

import org.swm.att.domain.entity.response.CategoriesVO


interface AttMenuRepository {
    suspend fun getMenu(storeId: Int): Result<CategoriesVO>
}