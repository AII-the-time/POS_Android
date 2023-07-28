package org.swm.att.domain.entity.response

data class MenuVO(
    val id: Int,
    val storeId: Int,
    val name: String,
    val price: Int,
    val category: String
)
