package org.swm.att.domain.entity.request

import org.swm.att.domain.constant.PayMethod

data class PaymentVO(
    val paymentMethod: PayMethod,
    val price: Int,
    val orderId: Int
)