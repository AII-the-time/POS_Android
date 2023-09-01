package org.swm.att.data.remote.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import org.swm.att.domain.constant.PayMethod
import org.swm.att.domain.entity.response.PaymentOfBillVO

@JsonClass(generateAdapter = true)
data class PaymentOfBillDTO(
    @field:Json(name = "paymentMethod")
    val paymentMethod: String?,
    @field:Json(name = "price")
    val price: String?
) {
    fun toVO() = PaymentOfBillVO(
        paymentMethod = PayMethod.toPayMethod(paymentMethod ?: "Unknown"),
        price = price?.toBigDecimal() ?: (-1).toBigDecimal()
    )
}