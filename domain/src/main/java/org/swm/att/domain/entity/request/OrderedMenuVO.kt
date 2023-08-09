package org.swm.att.domain.entity.request

import kotlinx.serialization.Serializable
import org.swm.att.domain.entity.response.MenuVO

@Serializable
data class OrderedMenuVO(
    val menu: MenuVO,
    val count: Int? = 0
): java.io.Serializable
