package org.swm.att.data.remote.model.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import org.swm.att.domain.entity.response.MenuVO

@JsonClass(generateAdapter = true)
data class MenusDTO(
    @field:Json(name = "menus")
    val menus: List<MenuDTO>
) {
    fun toVO(): List<org.swm.att.domain.entity.response.MenuVO> {
        return menus.map { it.toVO() }
    }
}