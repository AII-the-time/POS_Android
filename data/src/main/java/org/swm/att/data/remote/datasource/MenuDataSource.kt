package org.swm.att.data.remote.datasource

import kotlinx.coroutines.flow.Flow
import org.swm.att.data.remote.request.CategoryPostDTO
import org.swm.att.data.remote.request.NewMenuDTO
import org.swm.att.data.remote.response.CategoriesDTO
import org.swm.att.data.remote.response.CategoryIdDTO
import org.swm.att.data.remote.response.MenuIdDTO
import org.swm.att.data.remote.response.MenuWithRecipeDTO
import org.swm.att.data.remote.response.OptionListDTO
import org.swm.att.data.remote.response.StockDTO
import org.swm.att.data.remote.response.StockIdDTO
import org.swm.att.data.remote.response.StocksDTO
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

    suspend fun postCategory(storeId: Int, categoryPostInfo: CategoryPostDTO): Flow<CategoryIdDTO> {
        return checkResponse(attPosService.postCategory(storeId, categoryPostInfo))
    }

    suspend fun postNewMenu(storeId: Int, newMenu: NewMenuDTO): Flow<MenuIdDTO> {
        return checkResponse(attPosService.postNewMenu(storeId, newMenu))
    }

    suspend fun getAllOFOption(storeId: Int): Flow<OptionListDTO> {
        return checkResponse(attPosService.getAllOfOptions(storeId))
    }

    suspend fun getAllOfStocks(storeId: Int, name: String): Flow<StocksDTO> {
        return checkResponse(attPosService.getAllOfStocks(storeId, name))
    }

    suspend fun postNewStock(storeId: Int, newStock: StockDTO): Flow<StockIdDTO> {
        return checkResponse(attPosService.postNewStock(storeId, newStock))
    }
}