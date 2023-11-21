package org.swm.att.domain.sever_driven_ui.response.text

import org.swm.att.domain.sever_driven_ui.response.SduiReportContent

data class ReportTextVO (
    val align: String,
    val textItems: List<ReportTextItemVO>
): SduiReportContent