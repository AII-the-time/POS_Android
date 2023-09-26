package org.swm.att.domain.entity.response

import java.math.BigDecimal

data class MenuWithRecipeVO(
    val categoryId: Int,
    val categoryName: String,
    val menuName: String,
    val price: BigDecimal,
    val option: List<OptionVO>,
    val recipe: List<RecipeVO>
)