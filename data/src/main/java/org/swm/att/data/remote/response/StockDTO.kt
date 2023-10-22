package org.swm.att.data.remote.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import org.swm.att.domain.entity.response.StockVO

@JsonClass(generateAdapter = true)
data class StockDTO(
    @field:Json(name = "name")
    val name: String?,
    @field:Json(name = "amount")
    val amount: Int?,
    @field:Json(name = "unit")
    val unit: String?,
    @field:Json(name = "price")
    val price: String?,
    @field:Json(name = "currentAmount")
    val currentAmount: Int?,
    @field:Json(name = "noticeThreshold")
    val noticeThreshold: Int?,
    @field:Json(name = "updatedAt")
    val updatedAt: String?
) {
    fun toVO() = StockVO(
        name = name ?: "Unknown",
        amount = amount ?: -1,
        unit = unit ?: "g",
        price = price ?: "Unknown",
        currentAmount = currentAmount ?: -1,
        noticeThreshold = noticeThreshold ?: -1,
        updatedAt = updatedAt ?: "Unknown"
    )
}
