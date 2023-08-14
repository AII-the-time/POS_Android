package org.swm.att.data.remote.datasource

import org.swm.att.data.remote.request.OrderedMenusDTO
import org.swm.att.data.remote.response.OrderDTO
import org.swm.att.data.remote.service.AttPosService
import javax.inject.Inject

class OrderDataSource @Inject constructor(
    private val attPosService: AttPosService
): BaseNetworkDataSource() {
    suspend fun postOrder(storeId: Int, orderedMenus: OrderedMenusDTO): OrderDTO {
        return checkResponse(attPosService.postOrder(storeId, orderedMenus))
    }
}