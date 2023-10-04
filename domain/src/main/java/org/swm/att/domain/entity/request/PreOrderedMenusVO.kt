package org.swm.att.domain.entity.request

data class PreOrderedMenusVO(
    val totalPrice: String,
    val menus: List<OrderedMenuVO>?,
    val phone: String?,
    val memo: String?,
    val orderedFor: String?
)