package org.swm.att.data.remote.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import org.swm.att.domain.entity.response.StocksVO

@JsonClass(generateAdapter = true)
data class StocksDTO(
    @field:Json(name = "stocks")
    val stocks: List<StockDTO>?
) {
    fun toVO() = StocksVO(
        stocks = stocks?.map { it.toVO() } ?: listOf()
    )
}
