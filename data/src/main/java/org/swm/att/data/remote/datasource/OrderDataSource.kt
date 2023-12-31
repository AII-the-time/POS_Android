package org.swm.att.data.remote.datasource

import kotlinx.coroutines.flow.Flow
import org.swm.att.data.remote.request.OrderedMenusDTO
import org.swm.att.data.remote.request.PaymentDTO
import org.swm.att.data.remote.request.PreOrderedMenusDTO
import org.swm.att.data.remote.response.OrderBillsDTO
import org.swm.att.data.remote.response.OrderDTO
import org.swm.att.data.remote.response.OrderIdDTO
import org.swm.att.data.remote.response.OrderReceiptDTO
import org.swm.att.data.remote.response.PreOrderBillDTO
import org.swm.att.data.remote.response.PreOrdersDTO
import org.swm.att.data.remote.response.PreorderIdDTO
import org.swm.att.data.remote.service.AttPosService
import org.swm.att.domain.entity.response.OrderIdVO
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

    suspend fun getOrderBills(
        storeId: Int,
        page: Int,
        date: String?,
        count: Int
    ): Flow<OrderBillsDTO> {
        return checkResponse(attPosService.getOrderBills(storeId, page, date, count))
    }

    suspend fun getOrderBill(storeId: Int, orderId: Int): Flow<OrderReceiptDTO> {
        return checkResponse(attPosService.getOrderBill(storeId, orderId))
    }

    suspend fun postPreOrder(storeId: Int, preOrderedMenus: PreOrderedMenusDTO): Flow<PreorderIdDTO> {
        return checkResponse(attPosService.postPreOrder(storeId, preOrderedMenus))
    }

    suspend fun getPreOrders(storeId: Int, page: Int, date: String?): Flow<PreOrdersDTO> {
        return checkResponse(attPosService.getPreOrders(storeId, page, 6, date))
    }

    suspend fun getPreOrderBill(storeId: Int, preOrderId: Int): Flow<PreOrderBillDTO> {
        return checkResponse(attPosService.getPreOrderBill(storeId, preOrderId))
    }

    suspend fun deletePreorder(storeId: Int, preorderId: Int): Flow<PreorderIdDTO> {
        return checkResponse(attPosService.deletePreorder(storeId, preorderId))
    }

    suspend fun updatePreorder(storeId: Int, preOrderedMenus: PreOrderedMenusDTO): Flow<PreorderIdDTO> {
        return checkResponse(attPosService.updatePreorder(storeId, preOrderedMenus))
    }

    suspend fun cancelOrder(store:Int, orderId: Int): Flow<OrderIdDTO> {
        return checkResponse(attPosService.cancelOrder(store, orderId))
    }
}