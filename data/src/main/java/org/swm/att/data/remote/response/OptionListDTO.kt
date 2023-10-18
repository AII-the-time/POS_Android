package org.swm.att.data.remote.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import org.swm.att.domain.entity.response.OptionListVO

@JsonClass(generateAdapter = true)
data class OptionListDTO(
    @field:Json(name = "option")
    val option: List<OptionDTO>?
) {
    fun toVO() = OptionListVO(
        option = option?.map { it.toVO() }
    )
}