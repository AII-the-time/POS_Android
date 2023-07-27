package org.swm.att.data.remote.datasource

import org.swm.att.data.remote.response.MenusDTO
import org.swm.att.data.remote.service.AttPosService
import javax.inject.Inject

class MenuDataSource @Inject constructor(
    private val attPosService: AttPosService
) : BaseNetworkDataSource() {

    suspend fun getMenu(storeId: Int): MenusDTO {
        return checkResponse(attPosService.getMenu(storeId))
    }
}