package org.swm.att.data.remote.request

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class OrderedMenuDTO(
    @field:Json(name = "id")
    val id: Int,
    @field:Json(name = "count")
    val count: Int,
    @field:Json(name = "options")
    val options: List<Int>?,
    @field:Json(name = "detail")
    val detail: String? = null
)