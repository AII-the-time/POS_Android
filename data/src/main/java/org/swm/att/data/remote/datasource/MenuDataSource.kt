package org.swm.att.data.remote.datasource

import kotlinx.coroutines.flow.Flow
import org.swm.att.data.remote.request.CategoryPostDTO
import org.swm.att.data.remote.response.CategoriesDTO
import org.swm.att.data.remote.response.MenuWithRecipeDTO
import org.swm.att.data.remote.service.AttPosService
import javax.inject.Inject

class MenuDataSource @Inject constructor(
    private val attPosService: AttPosService
) : BaseNetworkDataSource() {

    suspend fun getMenu(storeId: Int): Flow<CategoriesDTO> {
        return checkResponse(attPosService.getMenu(storeId))
    }

    suspend fun getMenuInfo(storeId: Int, menuId: Int): Flow<MenuWithRecipeDTO> {
        return checkResponse(attPosService.getMenuInfo(storeId, menuId))
    }

    suspend fun postCategory(storeId: Int, categoryPostInfo: CategoryPostDTO): Flow<Unit> {
        return checkResponse(attPosService.postCategory(storeId, categoryPostInfo))
    }
}