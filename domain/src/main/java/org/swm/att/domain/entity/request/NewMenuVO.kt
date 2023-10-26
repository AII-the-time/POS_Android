package org.swm.att.domain.entity.request

import org.swm.att.domain.entity.response.RecipeVO

data class NewMenuVO(
    val id: Int? = null,
    val name: String,
    val price: Int,
    val categoryId: Int,
    val option: List<Int>? = null,
    val recipe: List<RecipeVO>? = null
)