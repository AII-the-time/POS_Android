package org.swm.att.domain.entity.response

import java.math.BigDecimal

data class OrderedMenuOfBillVO(
    val id: Int,
    val count: Int,
    val price: BigDecimal,
    val menuName: String,
    val options: List<OptionTypeVO>,
    val detail: String
)
