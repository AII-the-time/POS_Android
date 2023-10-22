package org.swm.att.data.remote.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import org.swm.att.domain.entity.response.StockWithStateListVO

@JsonClass(generateAdapter = true)
data class StockWithStateListDTO(
    @field:Json(name = "stocks")
    val stocks: List<StockWithStateDTO>?
) {
    fun toVO() = StockWithStateListVO(
        stocks = stocks?.map { it.toVO() } ?: listOf()
    )
}
