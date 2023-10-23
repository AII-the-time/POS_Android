package org.swm.att.domain.entity.response

data class StockVO(
    val id: Int? = -1,
    val name: String,
    val amount: Int,
    val unit: String,
    val price: String,
    val currentAmount: Int,
    val noticeThreshold: Int,
    val updatedAt: String,
    val menus: List<String>? = null
)