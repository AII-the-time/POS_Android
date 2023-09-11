package org.swm.att.domain.repository

import kotlinx.coroutines.flow.Flow
import org.swm.att.domain.entity.request.OrderedMenusVO
import org.swm.att.domain.entity.request.PaymentVO
import org.swm.att.domain.entity.response.OrderVO
import org.swm.att.domain.entity.response.PaymentResultVO

interface AttOrderRepository {
    suspend fun postOrder(storeId: Int, mileageId: Int?, totawlPrice: Int, orderedMenus: OrderedMenusVO): Flow<Result<OrderVO>>
    suspend fun postPayment(storeId: Int, paymentVO: PaymentVO): Flow<Result<PaymentResultVO>>
}