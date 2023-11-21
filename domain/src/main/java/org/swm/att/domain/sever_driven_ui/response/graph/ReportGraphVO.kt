package org.swm.att.domain.sever_driven_ui.response.graph

import org.swm.att.domain.sever_driven_ui.response.SduiReportContent

data class ReportGraphVO(
    val graphTitle: String,
    val graphColor: String,
    val graphItems: List<ReportGraphItemVO>
): SduiReportContent