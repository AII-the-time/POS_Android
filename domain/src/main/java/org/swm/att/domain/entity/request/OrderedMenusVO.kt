package org.swm.att.domain.entity.request

import kotlinx.serialization.Serializable

@Serializable
data class OrderedMenusVO(
    val menus: List<OrderedMenuVO>? = listOf()
): java.io.Serializable

