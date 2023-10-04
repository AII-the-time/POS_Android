package org.swm.att.domain.entity.response

data class PreorderVO(
    val preOrderId: Int,
    val createdAt: String,
    val orderedFor: String,
    val totalPrice: String,
    val totalCount: Int,
    val phone: String,
    val memo: String? = null,
)
