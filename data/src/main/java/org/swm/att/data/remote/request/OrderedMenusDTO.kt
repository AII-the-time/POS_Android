package org.swm.att.data.remote.request

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class OrderedMenusDTO(
    @field:Json(name = "totalPrice")
    val totalPrice: Int,
    @field:Json(name = "menus")
    val menus: List<OrderedMenuDTO>?
)
