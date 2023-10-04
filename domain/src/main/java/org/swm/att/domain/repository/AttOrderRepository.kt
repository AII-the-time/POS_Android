package org.swm.att.domain.repository

import kotlinx.coroutines.flow.Flow
import org.swm.att.domain.entity.request.OrderedMenusVO
import org.swm.att.domain.entity.request.PaymentVO
import org.swm.att.domain.entity.request.PreOrderedMenusVO
import org.swm.att.domain.entity.response.OrderBillsVO
import org.swm.att.domain.entity.response.OrderReceiptVO
import org.swm.att.domain.entity.response.OrderVO

interface AttOrderRepository {
    suspend fun postOrder(storeId: Int, totalPrice: Int, orderedMenus: OrderedMenusVO): Flow<Result<OrderVO>>
    suspend fun postPayment(storeId: Int, paymentVO: PaymentVO): Flow<Result<Nothing?>>
    suspend fun getOrderBills(storeId: Int, page: Int, count: Int): Flow<Result<OrderBillsVO>>
    suspend fun getOrderBill(storeId: Int, orderId: Int): Flow<Result<OrderReceiptVO>>
    suspend fun postPreOrder(storeId: Int, preOrderedMenus: PreOrderedMenusVO): Flow<Result<Unit>>
}