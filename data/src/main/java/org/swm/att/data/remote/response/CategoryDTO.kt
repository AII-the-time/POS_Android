package org.swm.att.data.remote.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import org.swm.att.domain.entity.response.CategoryVO

@JsonClass(generateAdapter = true)
data class CategoryDTO(
    @field:Json(name = "category")
    val category: String?,
    @field:Json(name = "menus")
    val menus: List<MenuDTO>?
) {
    fun toVO(): CategoryVO {
        return CategoryVO(
            category ?: "unknown",
            menus?.map { it.toVO() } ?: listOf()
        )
    }
}