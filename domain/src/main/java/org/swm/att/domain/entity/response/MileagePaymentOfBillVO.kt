package org.swm.att.domain.entity.response

import java.math.BigDecimal

data class MileagePaymentOfBillVO(
    val use: BigDecimal,
    val save: BigDecimal
)