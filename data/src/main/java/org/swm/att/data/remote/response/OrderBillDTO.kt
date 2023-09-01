package org.swm.att.data.remote.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import org.swm.att.domain.constant.PayMethod
import org.swm.att.domain.constant.PayState
import org.swm.att.domain.entity.response.OrderBillVO

@JsonClass(generateAdapter = true)
data class OrderBillDTO(
    @field:Json(name = "orderId")
    val orderId: Int?,
    @field:Json(name = "paymentStatus")
    val paymentState: String?,
    @field:Json(name = "paymentMethod")
    val paymentMethod: String?,
    @field:Json(name = "totalCount")
    val totalCount: Int?,
    @field:Json(name = "totalPrice")
    val totalPrice: String?,
    @field:Json(name = "createdAt")
    val createdAt: String?
) {
    fun toVO() = OrderBillVO(
        orderId = orderId ?: -1,
        paymentState = PayState.toPayState(paymentState ?: "Unknown"),
        paymentMethod = PayMethod.toPayMethod(paymentMethod ?: "Unknown"),
        totalCount = totalCount ?: -1,
        totalPrice = totalPrice?.toBigDecimal() ?: (-1).toBigDecimal(),
        createdAt = createdAt ?: "Unknown"
    )
}
