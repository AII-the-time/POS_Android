package org.swm.att.data.remote.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import org.swm.att.domain.entity.response.OptionTypeVO

@JsonClass(generateAdapter = true)
data class OptionTypeDTO(
    @field:Json(name = "id")
    val id: Int?,
    @field:Json(name = "name")
    val name: String?,
    @field:Json(name = "price")
    val price: Int?
) {
    fun toVO(): OptionTypeVO {
        return OptionTypeVO(
            id ?: -1,
            name ?: "",
            price ?: -1
        )
    }
}
