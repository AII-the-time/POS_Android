package org.swm.att.data.remote.request

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import org.swm.att.data.remote.response.RecipeDTO

@JsonClass(generateAdapter = true)
data class NewMenuDTO(
    @field:Json(name = "name")
    val name: String?,
    @field:Json(name = "price")
    val price: Int?,
    @field:Json(name = "categoryId")
    val categoryId: Int?,
    @field:Json(name = "option")
    val option: List<Int>?,
    @field:Json(name = "recipe")
    val recipe: List<RecipeDTO>?
)
