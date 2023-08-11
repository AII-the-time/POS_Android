package org.swm.att.domain.entity.response

import kotlinx.serialization.Serializable

@Serializable
data class OptionTypeVO(
    val id: Int,
    val name: String,
    val price: Int
): java.io.Serializable