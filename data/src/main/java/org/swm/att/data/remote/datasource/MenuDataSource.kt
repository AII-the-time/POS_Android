package org.swm.att.data.remote.datasource

import org.swm.att.data.remote.response.CategoriesDTO
import org.swm.att.data.remote.response.CategoryDTO
import org.swm.att.data.remote.service.AttPosService
import javax.inject.Inject

class MenuDataSource @Inject constructor(
    private val attPosService: AttPosService
) : BaseNetworkDataSource() {

    suspend fun getMenu(storeId: Int): CategoriesDTO {
        return checkResponse(attPosService.getMenu(storeId))
    }
}