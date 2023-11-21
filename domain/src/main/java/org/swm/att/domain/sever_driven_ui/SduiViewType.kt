package org.swm.att.domain.sever_driven_ui

import org.swm.att.domain.sever_driven_ui.response.ReportUnknownVO
import org.swm.att.domain.sever_driven_ui.response.graph.ReportGraphVO
import org.swm.att.domain.sever_driven_ui.response.piechart.ReportPieChartVO
import org.swm.att.domain.sever_driven_ui.response.text.ReportTextVO
import java.lang.reflect.Type

enum class SduiViewType(
    val viewType: String, private val viewTypeClass: Type
) {
    PIECHART("piechart", ReportPieChartVO::class.java),
    GRAPH("graph", ReportGraphVO::class.java),
    TEXT("text", ReportTextVO::class.java),
    UNKNOWN("unknown", ReportUnknownVO::class.java);

    companion object {
        fun getViewTypeByOrdinal(ordinalNum: Int): SduiViewType {
            return values()[ordinalNum]
        }

        fun SduiViewType.getViewTypeClass(): Type {
            return this.viewTypeClass
        }

        fun findClassByItsName(viewTypeString: String?): SduiViewType {
            values().find { it.viewType == viewTypeString }?.let {
                return it
            } ?: return UNKNOWN
        }
    }
}