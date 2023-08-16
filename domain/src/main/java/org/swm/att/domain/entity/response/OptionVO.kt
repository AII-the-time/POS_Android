package org.swm.att.domain.entity.response

import kotlinx.serialization.Serializable

@Serializable
data class OptionVO(
    val id: String,
    val optionType: String,
    val options: List<OptionTypeVO>
): java.io.Serializable