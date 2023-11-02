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
    val updatedAt: String?,
    @field:Json(name = "menus")
    val menus: List<String>?
) {
    fun toVO() = StockVO(
        id = id,
        name = name ?: "Unknown",
        amount = amount ?: 0,
        unit = unit ?: "-",
        price = price ?: "Unknown",
        currentAmount = currentAmount ?: 0,
        noticeThreshold = noticeThreshold ?: -1,
        updatedAt = updatedAt ?: "Unknown",
        menus = menus ?: listOf()
    )
}
