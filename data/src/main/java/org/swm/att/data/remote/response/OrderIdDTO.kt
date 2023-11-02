package org.swm.att.data.remote.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import org.swm.att.domain.entity.response.OrderIdVO

@JsonClass(generateAdapter = true)
data class OrderIdDTO(
    @field:Json(name = "id")
    val id: Int?
) {
    fun toVO() = OrderIdVO(
        id = id ?: -1
    )
}