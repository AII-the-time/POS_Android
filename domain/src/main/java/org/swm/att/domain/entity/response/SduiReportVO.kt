package org.swm.att.domain.entity.response

data class SduiReportVO(
    val screenName: String,
    val viewContents: List<SduiReportItemVO>
)