package org.swm.att.domain.sever_driven_ui.response.piechart

import org.swm.att.domain.sever_driven_ui.response.SduiReportContent

data class ReportPieChartVO (
    val pieChartTitle: String,
    val totalCount: Int,
    val pieChartItems: List<ReportPieChartItemVO>
): SduiReportContent