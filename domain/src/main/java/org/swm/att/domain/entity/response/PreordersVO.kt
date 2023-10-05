package org.swm.att.domain.entity.response

data class PreOrdersVO(
    val endPage: Int,
    val preOrders: List<PreorderVO>
)