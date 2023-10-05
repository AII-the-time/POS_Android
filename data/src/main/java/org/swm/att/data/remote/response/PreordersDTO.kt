package org.swm.att.data.remote.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import org.swm.att.domain.entity.response.PreOrdersVO

@JsonClass(generateAdapter = true)
data class PreOrdersDTO(
    @field:Json(name = "endPage")
    val endPage: Int?,
    @field:Json(name = "preOrders")
    val preOrders: List<PreOrderDTO>?
) {
    fun toVO() = PreOrdersVO(
        endPage = endPage ?: -1,
        preOrders = preOrders?.map { it.toVO() } ?: listOf()
    )
}