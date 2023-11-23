package org.swm.att.data.remote.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import org.swm.att.domain.entity.response.HistoryVO

@JsonClass(generateAdapter = true)
data class HistoryDTO(
    @field:Json(name = "amount")
    val amount: Int?,
    @field:Json(name = "date")
    val date: String?,
    @field:Json(name = "price")
    val price: String?
) {
    fun toVO() = HistoryVO(
        amount = amount ?: 0,
        date = date ?: "Unknown",
        price = price ?: "0"
    )
}