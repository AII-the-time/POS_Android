package org.swm.att.data.remote.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import org.swm.att.domain.constant.PayMethod
import org.swm.att.domain.constant.PayState
import org.swm.att.domain.entity.response.MileagePaymentOfBillVO
import org.swm.att.domain.entity.response.OrderReceiptVO
import org.swm.att.domain.entity.response.PaymentOfBillVO

@JsonClass(generateAdapter = true)
data class OrderReceiptDTO(
    @field:Json(name = "paymentStatus")
    val paymentState: String?,
    @field:Json(name = "totalPrice")
    val totalPrice: String?,
    @field:Json(name = "createdAt")
    val createdAt: String?,
    @field:Json(name = "orderitems")
    val orderItems: List<OrderedMenuOfBillDTO>,
    @field:Json(name = "mileage")
    val mileage: MileagePaymentOfBillDTO?,
    @field:Json(name = "pay")
    val pay: PaymentOfBillDTO?
) {
    fun toVO() = OrderReceiptVO(
        paymentState = PayState.toPayState(paymentState ?: "Unknown"),
        totalPrice = totalPrice?.toBigDecimal() ?: (-1).toBigDecimal(),
        createdAt = createdAt ?: "Unknown",
        orderItems = orderItems.map { it.toVO() },
        mileage = mileage?.toVO() ?: MileagePaymentOfBillVO((-1).toBigDecimal(), (-1).toBigDecimal()),
        pay = pay?.toVO() ?: PaymentOfBillVO(PayMethod.UNKNOWN, (-1).toBigDecimal())
    )
}
