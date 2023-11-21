package org.swm.att.domain.entity.response

import org.swm.att.domain.sever_driven_ui.SduiViewType
import org.swm.att.domain.sever_driven_ui.response.SduiReportContent
import org.swm.att.domain.sever_driven_ui.response.graph.ReportGraphVO
import org.swm.att.domain.sever_driven_ui.response.piechart.ReportPieChartVO

interface SduiReportItemVO {
    val viewType: SduiViewType
    val content: SduiReportContent
}

data class SduiReportGraphItemVO(
    override val viewType: SduiViewType,
    override val content: ReportGraphVO
): SduiReportItemVO

data class SduiReportPieChartItemVO(
    override val viewType: SduiViewType,
    override val content: ReportPieChartVO
): SduiReportItemVO