package org.swm.att.domain.repository

import kotlinx.coroutines.flow.Flow
import org.swm.att.domain.entity.request.OrderedMenusVO
import org.swm.att.domain.entity.request.PaymentVO
import org.swm.att.domain.entity.response.OrderVO

interface AttOrderRepository {
    suspend fun postOrder(storeId: Int, totalPrice: Int, orderedMenus: OrderedMenusVO): Flow<Result<OrderVO>>
    suspend fun postPayment(storeId: Int, paymentVO: PaymentVO): Flow<Result<Nothing?>>
}