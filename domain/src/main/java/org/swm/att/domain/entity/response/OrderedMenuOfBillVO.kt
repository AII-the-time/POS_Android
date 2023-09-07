package org.swm.att.domain.entity.response

import kotlinx.serialization.Serializable
import org.swm.att.domain.util.BigDecimalSerializer
import java.math.BigDecimal

@Serializable
data class OrderedMenuOfBillVO(
    val id: Int,
    val count: Int,
    @Serializable(with = BigDecimalSerializer::class)
    val price: BigDecimal,
    val menuName: String,
    val options: List<OptionTypeVO>,
    val detail: String
): java.io.Serializable
