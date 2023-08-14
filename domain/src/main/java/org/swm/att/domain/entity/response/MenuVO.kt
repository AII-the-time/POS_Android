package org.swm.att.domain.entity.response

import kotlinx.serialization.Serializable

@Serializable
data class MenuVO(
    val id: Int,
    val name: String,
    val price: Int,
    val option: List<OptionVO>,
    val detail: String? = null
): java.io.Serializable