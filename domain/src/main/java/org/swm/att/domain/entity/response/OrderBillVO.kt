package org.swm.att.domain.entity.response

import org.swm.att.domain.constant.PayMethod
import org.swm.att.domain.constant.PayState
import java.math.BigDecimal

data class OrderBillVO(
    val orderId: Int,
    val paymentState: PayState,
    val paymentMethod: PayMethod,
    val totalCount: Int,
    val totalPrice: BigDecimal,
    val createdAt: String
)