package org.swm.att.domain.entity.response

import org.swm.att.domain.constant.PayMethod
import java.math.BigDecimal

data class PaymentOfBillVO(
    val paymentMethod: PayMethod,
    val price: BigDecimal
)