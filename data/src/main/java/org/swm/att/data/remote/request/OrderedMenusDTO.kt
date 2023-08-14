package org.swm.att.data.remote.request

data class OrderedMenusDTO(
    val totalPrice: Int,
    val mileageId: Int?,
    val menus: List<OrderedMenuDTO>?
)
