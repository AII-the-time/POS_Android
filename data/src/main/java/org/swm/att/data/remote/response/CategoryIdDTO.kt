package org.swm.att.data.remote.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import org.swm.att.domain.entity.response.CategoryIdVO

@JsonClass(generateAdapter = true)
data class CategoryIdDTO(
    @field:Json(name = "categoryId")
    val categoryId: Int?
) {
    fun toVO() = CategoryIdVO(
        categoryId = categoryId ?: -1
    )
}