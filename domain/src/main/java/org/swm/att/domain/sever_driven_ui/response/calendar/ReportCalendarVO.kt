package org.swm.att.domain.sever_driven_ui.response.calendar

import org.swm.att.domain.sever_driven_ui.response.SduiReportContent

data class ReportCalendarVO(
    val calendarTitle: String,
    val calendarDate: String,
    val calendarItems: List<ReportCalendarItemVO>
): SduiReportContent