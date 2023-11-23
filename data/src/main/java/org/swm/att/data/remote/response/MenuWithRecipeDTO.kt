package org.swm.att.data.remote.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import org.swm.att.domain.entity.response.MenuWithRecipeVO

@JsonClass(generateAdapter = true)
data class MenuWithRecipeDTO(
    @field:Json(name = "categoryId")
    val categoryId: Int?,
    @field:Json(name = "category")
    val categoryName: String?,
    @field:Json(name = "name")
    val name: String?,
    @field:Json(name = "price")
    val price: String?,
    @field:Json(name = "option")
    val option: List<OptionDTO>?,
    @field:Json(name = "recipe")
    val recipe: List<RecipeDTO>?,
    @field:Json(name = "history")
    val history: List<HistoryDTO>?
) {
    fun toVO() = MenuWithRecipeVO(
        id = categoryId ?: -1,
        category = categoryName ?: "Unknown",
        menuName = name ?: "Unknown",
        price = price?.toBigDecimal() ?: "-1".toBigDecimal(),
        option = option?.map { it.toVO() } ?: emptyList(),
        recipe = recipe?.map { it.toVO() } ?: emptyList(),
        history = history?.map { it.toVO() } ?: emptyList()
    )
}
