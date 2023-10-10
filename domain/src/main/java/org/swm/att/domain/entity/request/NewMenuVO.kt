package org.swm.att.domain.entity.request

import org.swm.att.domain.entity.response.RecipeVO

data class NewMenuVO(
    val name: String,
    val price: Int,
    val categoryId: Int,
    val option: List<Int>,
    val recipe: List<RecipeVO>
)