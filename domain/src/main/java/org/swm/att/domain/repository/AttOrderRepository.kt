package org.swm.att.domain.repository

import kotlinx.coroutines.flow.Flow
import org.swm.att.domain.entity.request.OrderedMenusVO
import org.swm.att.domain.entity.request.PaymentVO
import org.swm.att.domain.entity.request.PreOrderedMenusVO
import org.swm.att.domain.entity.response.OrderBillsVO
import org.swm.att.domain.entity.response.OrderIdVO
import org.swm.att.domain.entity.response.OrderReceiptVO
import org.swm.att.domain.entity.response.OrderVO
import org.swm.att.domain.entity.response.PreOrderBillVO
import org.swm.att.domain.entity.response.PreOrdersVO
import org.swm.att.domain.entity.response.PreorderIdVO

interface AttOrderRepository {
    suspend fun postOrder(storeId: Int, preOrderId: Int?, totalPrice: Int, orderedMenus: OrderedMenusVO): Flow<Result<OrderVO>>
    suspend fun postPayment(storeId: Int, paymentVO: PaymentVO): Flow<Result<Nothing?>>
    suspend fun getOrderBills(storeId: Int, page: Int, date: String?, count: Int): Flow<Result<OrderBillsVO>>
    suspend fun getOrderBill(storeId: Int, orderId: Int): Flow<Result<OrderReceiptVO>>
    suspend fun postPreOrder(storeId: Int, preOrderedMenus: PreOrderedMenusVO): Flow<Result<PreorderIdVO>>
    suspend fun getPreOrders(storeId: Int, page: Int, date: String?): Flow<Result<PreOrdersVO>>
    suspend fun getPreOrderBill(storeId: Int, preOrderId: Int): Flow<Result<PreOrderBillVO>>
    suspend fun deletePreorder(storeId: Int, preorderId: Int): Flow<Result<PreorderIdVO>>
    suspend fun updatePreorder(storeId: Int, preOrderedMenus: PreOrderedMenusVO): Flow<Result<PreorderIdVO>>
    suspend fun deleteOrder(storeId: Int, orderId: Int): Flow<Result<OrderIdVO>>
}