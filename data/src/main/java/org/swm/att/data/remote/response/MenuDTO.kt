package org.swm.att.data.remote.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import org.swm.att.domain.entity.response.MenuVO

@JsonClass(generateAdapter = true)
data class MenuDTO(
    @field:Json(name = "id")
    val id: Int?,
    @field:Json(name = "name")
    val name: String?,
    @field:Json(name = "price")
    val price: Int?,
    @field:Json(name = "stockStatus")
    val stockState: String?,
    @field:Json(name = "option")
    val option: List<OptionDTO>?
) {
    fun toVO(): MenuVO {
        return MenuVO(
            id = id ?: -1,
            name = name ?: "",
            price = price ?: -1,
            stockState = stockState ?: "UNKNOWN",
            option = option?.map { it.toVO() } ?: listOf()
        )
    }
}
