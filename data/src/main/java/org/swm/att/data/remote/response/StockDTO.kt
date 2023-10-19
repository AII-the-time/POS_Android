package org.swm.att.data.remote.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import org.swm.att.domain.entity.response.StockVO

@JsonClass(generateAdapter = true)
data class StockDTO(
    @field:Json(name = "id")
    val id: Int?,
    @field:Json(name = "name")
    val name: String?,
    @field:Json(name = "isMixed")
    val isMixed: Boolean?
) {
    fun toVO() = StockVO(
        id = id ?: -1,
        name = name ?: "Unknown",
        isMixed = isMixed ?: false
    )
}