package org.swm.att.data.remote.request

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import org.swm.att.domain.entity.request.OpeningHourVO
import org.swm.att.domain.entity.request.StoreVO

@JsonClass(generateAdapter = true)
data class OpeningHourDTO(
    @field:Json(name = "day")
    val day: String?,
    @field:Json(name = "open")
    val open: String?,
    @field:Json(name = "close")
    val close: String?
) {
    fun toVO() = OpeningHourVO(
        day = day ?: "Unknown",
        open = open ?: "Unknown",
        close = close ?: "Unknown"
    )
}