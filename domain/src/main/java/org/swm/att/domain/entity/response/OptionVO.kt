package org.swm.att.domain.entity.response

import kotlinx.serialization.Serializable

@Serializable
data class OptionVO(
    val id: Int,
    val category: String,
    val types: List<OptionTypeVO>
): java.io.Serializable