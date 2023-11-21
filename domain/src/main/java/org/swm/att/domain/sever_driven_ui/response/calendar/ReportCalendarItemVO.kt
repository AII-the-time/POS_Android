package org.swm.att.domain.sever_driven_ui.response.calendar

import org.swm.att.domain.sever_driven_ui.response.SduiReportContent

data class ReportCalendarItemVO(
    val contentDate: String,
    val value: String
): SduiReportContent