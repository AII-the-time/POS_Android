package org.swm.att.domain.entity.request

import kotlinx.serialization.Serializable
import org.swm.att.domain.entity.response.OptionTypeVO

@Serializable
data class OrderedMenuVO(
    val id: Int,
    val name: String,
    val price: Int,
    val count: Int? = 1,
    val options: List<OptionTypeVO>,
    val detail: String? = null
): java.io.Serializable
