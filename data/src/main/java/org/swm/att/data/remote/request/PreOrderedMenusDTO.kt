package org.swm.att.data.remote.request

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PreOrderedMenusDTO(
    @field:Json(name = "id")
    val id: Int?,
    @field:Json(name = "totalPrice")
    val totalPrice: String,
    @field:Json(name = "menus")
    val menus: List<OrderedMenuDTO>?,
    @field:Json(name = "phone")
    val phone: String?,
    @field:Json(name = "memo")
    val memo: String?,
    @field:Json(name = "orderedFor")
    val orderedFor: String?
)