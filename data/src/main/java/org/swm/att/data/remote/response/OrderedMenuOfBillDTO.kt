package org.swm.att.data.remote.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import org.swm.att.domain.entity.response.OrderedMenuOfBillVO

@JsonClass(generateAdapter = true)
data class OrderedMenuOfBillDTO(
    @field:Json(name = "id")
    val id: Int?,
    @field:Json(name = "count")
    val count: Int?,
    @field:Json(name = "price")
    val price: String?,
    @field:Json(name = "menuName")
    val menuName: String?,
    @field:Json(name = "options")
    val options: List<OptionTypeDTO>?,
    @field:Json(name = "detail")
    val detail: String?
) {
    fun toVO() = OrderedMenuOfBillVO(
        id = id ?: -1,
        count = count ?: -1,
        price = price?.toBigDecimal() ?: (-1).toBigDecimal(),
        menuName = menuName ?: "Unknown",
        options = options?.map { it.toVO() } ?: listOf(),
        detail = detail ?: "Unknown"
    )
}