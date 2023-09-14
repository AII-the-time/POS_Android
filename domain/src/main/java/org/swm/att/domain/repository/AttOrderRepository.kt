package org.swm.att.domain.repository

import org.swm.att.domain.entity.request.OrderedMenusVO
import org.swm.att.domain.entity.request.PaymentVO
import org.swm.att.domain.entity.response.OrderVO
import org.swm.att.domain.entity.response.PaymentResultVO

interface AttOrderRepository {
    suspend fun postOrder(storeId: Int, totalPrice: Int, orderedMenus: OrderedMenusVO): Result<OrderVO>
    suspend fun postPayment(storeId: Int, paymentVO: PaymentVO): Result<PaymentResultVO>
}