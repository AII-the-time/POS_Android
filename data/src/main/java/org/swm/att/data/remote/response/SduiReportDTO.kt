package org.swm.att.data.remote.response

import org.swm.att.domain.entity.response.SduiReportItemVO
import org.swm.att.domain.entity.response.SduiReportVO

data class SduiReportDTO(
    val screenName: String?,
    val viewContents: List<SduiReportItemVO>?
) {
    fun toVO() = SduiReportVO(
        screenName = screenName ?: "Unknown",
        viewContents = this.viewContents ?: emptyList()
    )
}