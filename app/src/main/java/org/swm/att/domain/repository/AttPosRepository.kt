package org.swm.att.domain.repository

import org.swm.att.domain.entity.response.MenuVO

interface AttPosRepository {
    suspend fun getMenu(storeId: Int): Result<List<MenuVO>>
}