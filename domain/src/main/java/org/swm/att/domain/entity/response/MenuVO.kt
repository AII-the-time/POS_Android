package org.swm.att.domain.entity.response

import kotlinx.serialization.Serializable

@Serializable
data class MenuVO(
    val id: Int,
    val name: String,
    val price: Int,
    val options: List<OptionVO>
): java.io.Serializable