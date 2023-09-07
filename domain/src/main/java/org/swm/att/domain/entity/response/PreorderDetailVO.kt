package org.swm.att.domain.entity.response

import kotlinx.serialization.Serializable

@Serializable
data class PreorderDetailVO(
    val orderId: Int,
    val totalCount: Int,
    val totalPrice: String,
    val orderedAt: String,
    val phone: String,
    val memo: String,
    val orderItems: List<OrderedMenuOfBillVO>
): java.io.Serializable