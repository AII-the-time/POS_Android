package org.swm.att.data.remote.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import org.swm.att.domain.entity.response.CategoriesVO

@JsonClass(generateAdapter = true)
data class CategoriesDTO(
    @field:Json(name = "categories")
    val categories: List<CategoryDTO>?
) {
    fun toVO(): CategoriesVO {
        return CategoriesVO(categories?.map { it.toVO() } ?: listOf())
    }
}
