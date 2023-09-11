package org.swm.att.data.remote.datasource

import kotlinx.coroutines.flow.Flow
import org.swm.att.data.remote.request.OrderedMenusDTO
import org.swm.att.data.remote.request.PaymentDTO
import org.swm.att.data.remote.response.OrderDTO
import org.swm.att.data.remote.response.PaymentResultDTO
import org.swm.att.data.remote.service.AttPosService
import javax.inject.Inject

class OrderDataSource @Inject constructor(
    private val attPosService: AttPosService
): BaseNetworkDataSource() {
    suspend fun postOrder(storeId: Int, orderedMenus: OrderedMenusDTO): Flow<OrderDTO> {
        return checkResponse(attPosService.postOrder(storeId, orderedMenus))
    }

    suspend fun postPayment(storeId: Int, paymentDTO: PaymentDTO): Flow<PaymentResultDTO> {
        return checkResponse(attPosService.postPayment(storeId, paymentDTO))
    }
}