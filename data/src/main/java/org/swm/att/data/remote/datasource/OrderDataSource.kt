package org.swm.att.data.remote.datasource

import kotlinx.coroutines.flow.Flow
import org.swm.att.data.remote.request.OrderedMenusDTO
import org.swm.att.data.remote.request.PaymentDTO
import org.swm.att.data.remote.request.PreOrderedMenusDTO
import org.swm.att.data.remote.response.OrderBillsDTO
import org.swm.att.data.remote.response.OrderDTO
import org.swm.att.data.remote.response.OrderReceiptDTO
import org.swm.att.data.remote.service.AttPosService
import javax.inject.Inject

class OrderDataSource @Inject constructor(
    private val attPosService: AttPosService
): BaseNetworkDataSource() {
    suspend fun postOrder(storeId: Int, orderedMenus: OrderedMenusDTO): Flow<OrderDTO> {
        return checkResponse(attPosService.postOrder(storeId, orderedMenus))
    }

    suspend fun postPayment(storeId: Int, paymentDTO: PaymentDTO): Flow<Unit> {
        return checkResponse(attPosService.postPayment(storeId, paymentDTO))
    }

    suspend fun getOrderBills(storeId: Int, page: Int, count: Int): Flow<OrderBillsDTO> {
        return checkResponse(attPosService.getOrderBills(storeId, page, count))
    }

    suspend fun getOrderBill(storeId: Int, orderId: Int): Flow<OrderReceiptDTO> {
        return checkResponse(attPosService.getOrderBill(storeId, orderId))
    }

    suspend fun postPreOrder(storeId: Int, preOrderedMenus: PreOrderedMenusDTO): Flow<Unit> {
        return checkResponse(attPosService.postPreOrder(storeId, preOrderedMenus))
    }
}