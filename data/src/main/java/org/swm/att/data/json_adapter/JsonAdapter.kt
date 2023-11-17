package org.swm.att.data.json_adapter

import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.PolymorphicJsonAdapterFactory
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.swm.att.domain.sever_driven_ui.response.SduiReportContent
import org.swm.att.domain.sever_driven_ui.response.calendar.ReportCalendarVO
import org.swm.att.domain.sever_driven_ui.response.graph.ReportGraphVO
import org.swm.att.domain.sever_driven_ui.response.piechart.ReportPieChartVO

object JsonAdapter {
    private val sduiJsonAdapter: PolymorphicJsonAdapterFactory<SduiReportContent> = PolymorphicJsonAdapterFactory.of(SduiReportContent::class.java, "viewType")
        .withSubtype(ReportCalendarVO::class.java, "calendar")
        .withSubtype(ReportPieChartVO::class.java, "pieChart")
        .withSubtype(ReportGraphVO::class.java, "graph")

    val moshi: Moshi = Moshi.Builder()
        .add(sduiJsonAdapter)
        .add(KotlinJsonAdapterFactory())
        .build()
}