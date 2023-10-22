package org.swm.att.data.remote.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import org.swm.att.domain.entity.response.StockWithMixedVO

@JsonClass(generateAdapter = true)
data class StockWithMixtedDTO(
    @field:Json(name = "id")
    val id: Int?,
    @field:Json(name = "name")
    val name: String?,
    @field:Json(name = "isMixed")
    val isMixed: Boolean?
) {
    fun toVO() = StockWithMixedVO(
        id = id ?: -1,
        name = name ?: "Unknown",
        isMixed = isMixed ?: false
    )
}