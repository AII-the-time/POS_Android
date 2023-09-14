package org.swm.att.domain.entity.request

import org.swm.att.domain.constant.PayMethod

data class PaymentVO(
    val orderId: Int,
    val paymentMethod: PayMethod,
    val mileageId: Int?,
    val useMileage: Int?,
    val saveMileage: Int?
)