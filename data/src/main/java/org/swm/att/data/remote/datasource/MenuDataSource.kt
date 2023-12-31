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
import org.swm.att.data.remote.response.StockWithMixedDTO
import org.swm.att.data.remote.response.StockIdDTO
import org.swm.att.data.remote.response.StockWithMixedListDTO
import org.swm.att.data.remote.response.StockWithStateListDTO
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

    suspend fun getAllOfStocks(storeId: Int, name: String): Flow<StockWithMixedListDTO> {
        return checkResponse(attPosService.getAllOfStocks(storeId, name))
    }

    suspend fun postNewStock(storeId: Int, newStock: StockWithMixedDTO): Flow<StockIdDTO> {
        return checkResponse(attPosService.postNewStock(storeId, newStock))
    }

    suspend fun getStockWithStateList(storeId: Int): Flow<StockWithStateListDTO> {
        return checkResponse(attPosService.getStockWithStateList(storeId))
    }

    suspend fun getStockById(storeId: Int, stockId: Int): Flow<StockDTO> {
        return checkResponse(attPosService.getStockById(storeId, stockId))
    }

    suspend fun postNewStock(storeId: Int, newStock: StockDTO): Flow<StockIdDTO> {
        return checkResponse(attPosService.postNewStock(storeId, newStock))
    }

    suspend fun updateStock(storeId: Int, stock: StockDTO): Flow<StockIdDTO> {
        return checkResponse(attPosService.updateStock(storeId, stock))
    }

    suspend fun deleteStock(storeId: Int, stockId: Int): Flow<StockIdDTO> {
        return checkResponse(attPosService.deleteStock(storeId, stockId))
    }

    suspend fun deleteMenu(storeId: Int, menuId: Int): Flow<MenuIdDTO> {
        return checkResponse(attPosService.deleteMenu(storeId, menuId))
    }

    suspend fun updateMenu(storeId: Int, menu: NewMenuDTO): Flow<MenuIdDTO> {
        return checkResponse(attPosService.updateMenu(storeId, menu))
    }

    suspend fun deleteCategory(storeId: Int, categoryId: Int): Flow<CategoryIdDTO> {
        return checkResponse(attPosService.deleteCategory(storeId, categoryId))
    }

    suspend fun updateCategory(storeId: Int, categoryInfo: CategoryPostDTO): Flow<CategoryIdDTO> {
        return checkResponse(attPosService.updateCategory(storeId, categoryInfo))
    }
}