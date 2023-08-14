package org.swm.att.data.remote.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import org.swm.att.domain.entity.response.OptionVO
import java.util.UUID

@JsonClass(generateAdapter = true)
data class OptionDTO(
    val id: String?,
    @field:Json(name = "optionType")
    val optionType: String?,
    @field:Json(name = "options")
    val options: List<OptionTypeDTO>?
) {
    fun toVO(): OptionVO {
        return OptionVO(
            UUID.randomUUID().toString(),
            optionType ?: "",
            options?.map { it.toVO() } ?: listOf()
        )
    }
}
