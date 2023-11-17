package org.swm.att.domain.entity.response

import org.swm.att.domain.sever_driven_ui.response.SduiReportContent

data class SduiReportItemVO(
    val viewType: String,
    val content: SduiReportContent
)