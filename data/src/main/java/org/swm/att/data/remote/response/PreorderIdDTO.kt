package org.swm.att.data.remote.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import org.swm.att.domain.entity.response.PreorderIdVO

@JsonClass(generateAdapter = true)
data class PreorderIdDTO(
    @field:Json(name = "preOrderId")
    val preOrderId: Int?
) {
    fun toVO() = PreorderIdVO(preOrderId = preOrderId ?: -1)
}