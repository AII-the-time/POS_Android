package org.swm.att.domain.entity.response

import kotlinx.serialization.Serializable

@Serializable
data class MenuVO(
    val id: Int,
    val storeId: Int,
    val name: String,
    val price: Int,
    val category: String
): java.io.Serializable