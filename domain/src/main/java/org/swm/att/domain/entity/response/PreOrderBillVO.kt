package org.swm.att.domain.entity.response

data class PreOrderBillVO(
    val totalPrice: String,
    val totalCount: Int,
    val createdAt: String,
    val orderedFor: String,
    val phone: String,
    val memo: String? = null,
    val orderitems: List<OrderedMenuOfBillVO>
)
