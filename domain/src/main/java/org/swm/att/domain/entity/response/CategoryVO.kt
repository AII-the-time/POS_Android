package org.swm.att.domain.entity.response

data class CategoryVO(
    val category: String,
    val categoryId: Int,
    val menus: List<MenuVO>
)
