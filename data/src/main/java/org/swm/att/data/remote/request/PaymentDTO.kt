package org.swm.att.data.remote.request

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PaymentDTO(
    @field:Json(name = "orderId")
    val orderId: Int,
    @field:Json(name = "paymentMethod")
    val paymentMethod: String,
    @field:Json(name = "mileageId")
    val mileageId: Int?,
    @field:Json(name = "useMileage")
    val useMileage: Int?,
    @field:Json(name = "saveMileage")
    val saveMileage: Int?
)