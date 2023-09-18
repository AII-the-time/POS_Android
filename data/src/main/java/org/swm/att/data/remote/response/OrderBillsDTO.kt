package org.swm.att.data.remote.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import org.swm.att.domain.entity.response.OrderBillsVO

@JsonClass(generateAdapter = true)
data class OrderBillsDTO(
    @field:Json(name = "orders")
    val orders: List<OrderBillDTO>
) {
    fun toVO() = OrderBillsVO(
        orders = orders.map { it.toVO() }
    )
}
