package org.swm.att.data.remote.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import org.swm.att.domain.entity.response.PreorderDetailVO

@JsonClass(generateAdapter = true)
data class PreorderDetailDTO(
    @field:Json(name = "orderId")
    val orderId: Int?,
    @field:Json(name = "totalCount")
    val totalCount: Int?,
    @field:Json(name = "totalPrice")
    val totalPrice: String?,
    @field:Json(name = "orderedAt")
    val orderedAt: String?,
    @field:Json(name = "phone")
    val phone: String?,
    @field:Json(name = "memo")
    val memo: String?,
    @field:Json(name = "orderItems")
    val orderItems: List<OrderedMenuOfBillDTO>?
) {
    fun toVO() = PreorderDetailVO(
        orderId = orderId ?: -1,
        totalCount = totalCount ?: -1,
        totalPrice = totalPrice ?: "Unknown",
        orderedAt = orderedAt ?: "Unknown",
        phone = phone ?: "Unknown",
        memo = memo ?: "Unknown",
        orderItems = orderItems?.map { it.toVO() } ?: emptyList()
    )
}
