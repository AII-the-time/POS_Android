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
    @field:Json(name = "isMixed")
    val isMixed: Boolean?,
    @field:Json(name = "coldRegularAmount")
    val coldRegularAmount: Int?,
    @field:Json(name = "unit")
    val unit: String?
) {
    fun toVO() = RecipeVO(
        id = id ?: -1,
        name = name ?: "Unknown",
        isMixed = isMixed ?: false,
        coldRegularAmount = coldRegularAmount.toString() ?: "-1",
        unit = unit ?: "Unknown"
    )
}
