package org.swm.att.data.remote.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import org.swm.att.domain.entity.response.SduiBaseResponseVO
import org.swm.att.domain.entity.response.SduiReportVO

@JsonClass(generateAdapter = true)
data class SduiBaseResponseDTO(
    @Json(name = "responseData")
    val responseData: SduiReportVO?,
) {
    fun toVO() = SduiBaseResponseVO(
        responseData = responseData ?: SduiReportVO("unknown", listOf())
    )
}