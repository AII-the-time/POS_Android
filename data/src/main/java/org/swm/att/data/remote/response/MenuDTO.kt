package org.swm.att.data.remote.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import org.swm.att.domain.entity.response.MenuVO

@JsonClass(generateAdapter = true)
data class MenuDTO(
    @field:Json(name = "id")
    val id: Int?,
    @field:Json(name = "storeId")
    val storeId: Int?,
    @field:Json(name = "name")
    val name: String?,
    @field:Json(name = "price")
    val price: Int?,
    @field:Json(name = "category")
    val category: String?
) {
    fun toVO(): MenuVO {
        return MenuVO(
            id ?: -1,
            storeId ?: -1,
            name ?: "",
            price ?: -1,
            category ?: "")
    }
}
