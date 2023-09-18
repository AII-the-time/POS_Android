package org.swm.att.domain.entity.response

import org.swm.att.domain.constant.PayState
import java.math.BigDecimal

data class OrderReceiptVO(
    val paymentState: PayState,
    val totalPrice: BigDecimal,
    val createdAt: String,
    val orderItems: List<OrderedMenuOfBillVO>,
    val mileage: MileagePaymentOfBillVO?,
    val pay: PaymentOfBillVO
)
