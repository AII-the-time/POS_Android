package org.swm.att.data.remote.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import org.swm.att.domain.entity.response.OptionVO

@JsonClass(generateAdapter = true)
data class OptionDTO(
    @field:Json(name = "id")
    val id: Int?,
    @field:Json(name = "category")
    val category: String?,
    @field:Json(name = "types")
    val types: List<OptionTypeDTO>?
) {
    fun toVO(): OptionVO {
        return OptionVO(
            id ?: -1,
            category ?: "",
            types?.map { it.toVO() } ?: listOf()
        )
    }
}
