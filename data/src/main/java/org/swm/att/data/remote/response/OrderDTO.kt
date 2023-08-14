package org.swm.att.data.remote.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import org.swm.att.domain.entity.response.OrderVO

@JsonClass(generateAdapter = true)
data class OrderDTO(
    @field:Json(name = "orderId")
    val orderId: Int?,
) {
    fun toOrderVO() = OrderVO(
        orderId = orderId ?: -1,
    )
}