package org.swm.att.data.remote.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import org.swm.att.domain.entity.response.StockWithMixedListVO

@JsonClass(generateAdapter = true)
data class StockWithMixedListDTO(
    @field:Json(name = "stocks")
    val stocks: List<StockWithMixedDTO>?
) {
    fun toVO() = StockWithMixedListVO(
        stocks = stocks?.map { it.toVO() } ?: listOf()
    )
}
