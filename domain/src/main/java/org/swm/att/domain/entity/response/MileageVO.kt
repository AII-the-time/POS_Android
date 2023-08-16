package org.swm.att.domain.entity.response

import kotlinx.serialization.Serializable

@Serializable
data class MileageVO(
    val mileageId: Int,
    val mileage: Int
): java.io.Serializable
