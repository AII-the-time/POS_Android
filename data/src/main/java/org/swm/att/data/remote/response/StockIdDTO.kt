package org.swm.att.data.remote.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import org.swm.att.domain.entity.response.StockIdVO

@JsonClass(generateAdapter = true)
data class StockIdDTO(
    @field:Json(name = "stockId")
    val stockId: Int?
) {
    fun toVO() = StockIdVO(
        stockId = stockId ?: -1
    )
}
