package org.swm.att.data.remote.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import org.swm.att.domain.entity.response.PreOrderBillVO

@JsonClass(generateAdapter = true)
data class PreOrderBillDTO(
    @field:Json(name = "totalPrice")
    val totalPrice: String?,
    @field:Json(name = "totalCount")
    val totalCount: Int?,
    @field:Json(name = "createdAt")
    val createdAt: String?,
    @field:Json(name = "orderedFor")
    val orderedFor: String?,
    @field:Json(name = "phone")
    val phone: String?,
    @field:Json(name = "memo")
    val memo: String?,
    @field:Json(name = "orderitems")
    val orderitems: List<OrderedMenuOfBillDTO>?
) {
    fun toVO() = PreOrderBillVO(
        totalPrice = totalPrice ?: "-1",
        totalCount = totalCount ?: -1,
        createdAt = createdAt ?: "Unknown",
        orderedFor = orderedFor ?: "Unknown",
        phone = phone ?: "Unknown",
        memo = memo,
        orderitems = orderitems?.map { it.toVO() } ?: listOf()
    )
}
