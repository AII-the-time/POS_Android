package org.swm.att.domain.entity.response

data class PreorderVO(
    val orderId: Int,
    val totalCount: Int,
    val totalPrice: String,
    val phone: String,
    val memo: String,
    val orderedAt: String
)
