package org.swm.att.data.remote.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import org.swm.att.domain.entity.response.StockWithStateVO

@JsonClass(generateAdapter = true)
data class StockWithStateDTO(
    @field:Json(name = "id")
    val id: Int?,
    @field:Json(name = "name")
    val name: String?,
    @field:Json(name = "status")
    val state: String?,
    @field:Json(name = "usingMenuCount")
    val usingMenuCount: Int?
) {
    fun toVO() = StockWithStateVO(
        id = id ?: -1,
        name = name ?: "Unknown",
        state = state ?: "Unknown",
        usingMenuCount = usingMenuCount ?: -1
    )
}
