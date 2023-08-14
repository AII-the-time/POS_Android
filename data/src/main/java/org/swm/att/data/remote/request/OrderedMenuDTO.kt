package org.swm.att.data.remote.request

import org.swm.att.data.remote.response.OptionDTO

data class OrderedMenuDTO(
    val Id: Int,
    val count: Int,
    val options: List<OptionDTO>?,
    val detail: String? = null
)