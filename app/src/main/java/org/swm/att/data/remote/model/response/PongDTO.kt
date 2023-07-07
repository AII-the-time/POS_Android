package org.swm.att.data.remote.model.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import org.swm.att.domain.entity.response.PongVO

@JsonClass(generateAdapter = true)
data class PongDTO(
    @field:Json(name = "data")
    val data: String
) {
    fun toVO(): PongVO {
        return PongVO(data)
    }
}
