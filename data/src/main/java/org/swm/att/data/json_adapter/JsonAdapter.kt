package org.swm.att.data.json_adapter

import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.PolymorphicJsonAdapterFactory
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.swm.att.domain.entity.response.SduiReportGraphItemVO
import org.swm.att.domain.entity.response.SduiReportItemVO
import org.swm.att.domain.entity.response.SduiReportPieChartItemVO
import org.swm.att.domain.entity.response.SduiReportTextItemVO

object JsonAdapter {
    private val sduiJsonAdapter: PolymorphicJsonAdapterFactory<SduiReportItemVO> = PolymorphicJsonAdapterFactory.of(SduiReportItemVO::class.java, "viewType")
        .withSubtype(SduiReportPieChartItemVO::class.java, "PIECHART")
        .withSubtype(SduiReportGraphItemVO::class.java, "GRAPH")
        .withSubtype(SduiReportTextItemVO::class.java, "TEXT")

    val moshi: Moshi = Moshi.Builder()
        .add(sduiJsonAdapter)
        .add(KotlinJsonAdapterFactory())
        .build()
}