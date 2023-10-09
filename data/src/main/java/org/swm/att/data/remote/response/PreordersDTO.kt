package org.swm.att.data.remote.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import org.swm.att.domain.entity.response.PreOrdersVO

@JsonClass(generateAdapter = true)
data class PreOrdersDTO(
    @field:Json(name = "lastPage")
    val lastPage: Int?,
    @field:Json(name = "totalPreOrderCount")
    val totalPreorderCount: Int?,
    @field:Json(name = "preOrders")
    val preOrders: List<PreOrderDTO>?
) {
    fun toVO() = PreOrdersVO(
        lastPage = lastPage ?: -1,
        totalPreorderCount = totalPreorderCount ?: -1,
        preOrders = preOrders?.map { it.toVO() } ?: listOf()
    )
}