package org.swm.att.data.remote.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import org.swm.att.domain.entity.response.RecipeVO

@JsonClass(generateAdapter = true)
data class RecipeDTO(
    @field:Json(name = "id")
    val id: Int?,
    @field:Json(name = "name")
    val name: String?,
    @field:Json(name = "amount")
    val amount: Int?,
    @field:Json(name = "unit")
    val unit: String?
) {
    fun toVO() = RecipeVO(
        id = id ?: -1,
        name = name ?: "Unknown",
        amount = amount ?: -1,
        unit = unit ?: "Unknown"
    )
}
