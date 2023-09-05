package org.swm.att.data.remote.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import org.swm.att.domain.entity.response.PreorderVO

@JsonClass(generateAdapter = true)
data class PreorderDTO(
    @field:Json(name = "orderId")
    val orderId: Int?,
    @field:Json(name = "totalCount")
    val totalCount: Int?,
    @field:Json(name = "totalPrice")
    val totalPrice: String?,
    @field:Json(name = "phone")
    val phone: String?,
    @field:Json(name = "memo")
    val memo: String?,
    @field:Json(name = "orderedAt")
    val orderedAt: String?
) {
    fun toVO() = PreorderVO(
        orderId = orderId ?: -1,
        totalCount = totalCount ?: -1,
        totalPrice = totalPrice ?: "Unknown",
        phone = phone ?: "Unknown",
        memo = memo ?: "Unknown",
        orderedAt = orderedAt ?: "Unknown"
    )
}
