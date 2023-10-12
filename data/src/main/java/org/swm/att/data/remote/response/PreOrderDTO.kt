package org.swm.att.data.remote.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import org.swm.att.domain.entity.response.PreorderVO

@JsonClass(generateAdapter = true)
data class PreOrderDTO(
    @field:Json(name = "preOrderId")
    val preOrderId: Int?,
    @field:Json(name = "createdAt")
    val createdAt: String?,
    @field:Json(name = "orderedFor")
    val orderedFor: String?,
    @field:Json(name = "totalPrice")
    val totalPrice: String?,
    @field:Json(name = "totalCount")
    val totalCount: Int?,
    @field:Json(name = "phone")
    val phone: String?,
    @field:Json(name = "memo")
    val memo: String?
) {
    fun toVO() = PreorderVO(
        id = preOrderId ?: -1,
        createdAt = createdAt ?: "Unknown",
        orderedFor = orderedFor ?: "Unknown",
        totalCount = totalCount ?: -1,
        totalPrice = totalPrice ?: "Unknown",
        phone = phone ?: "Unknown",
        memo = memo ?: "Unknown"
    )
}
