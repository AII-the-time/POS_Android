package org.swm.att.data.remote.model.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import org.swm.att.domain.entity.response.MenuVO
@JsonClass(generateAdapter = true)
data class MenuDTO(
    @field:Json(name = "name")
    val name: String,
    @field:Json(name = "price")
    val price: Int,
    @field:Json(name = "category")
    val category: String
) {
    fun toVO(): MenuVO {
        return MenuVO(name, price, category)
    }
}
