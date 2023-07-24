package org.swm.att.data.remote.model.response

import com.squareup.moshi.Json
import org.swm.att.domain.entity.response.MenuVO

data class MenusDTO(
    @field:Json(name = "menus")
    val menus: List<MenuDTO>
) {
    fun toVO(): List<MenuVO> {
        return menus.map { it.toVO() }
    }
}