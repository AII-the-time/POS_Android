package org.swm.att.data.remote.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import org.swm.att.domain.entity.response.PaymentResultVO

@JsonClass(generateAdapter = true)
data class PaymentResultDTO(
    @field:Json(name = "leftPrice")
    val leftPrice: Int?
) {
    fun toVO() = PaymentResultVO(
        leftPrice = leftPrice ?: -1
    )
}
