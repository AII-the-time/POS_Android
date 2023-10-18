package org.swm.att.domain.entity.response

data class PreOrdersVO(
    val lastPage: Int,
    val totalPreorderCount: Int,
    val preOrders: List<PreorderVO>
)