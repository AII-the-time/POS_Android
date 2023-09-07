package org.swm.att.data.remote.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import org.swm.att.domain.entity.response.PreorderListVO

@JsonClass(generateAdapter = true)
data class PreorderListDTO(
    @field:Json(name = "orders")
    val orders: List<PreorderDTO>?
) {
    fun toVO() = PreorderListVO(orders?.map { it.toVO() } ?: emptyList())
}
