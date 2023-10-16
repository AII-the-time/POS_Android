package org.swm.att.data.remote.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import org.swm.att.domain.entity.response.MenuIdVO

@JsonClass(generateAdapter = true)
data class MenuIdDTO(
    @field:Json(name = "menuId")
    val menuId: Int?
) {
    fun toVO() = MenuIdVO(
        menuId = menuId ?: -1
    )
}