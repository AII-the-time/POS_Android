package org.swm.att.domain.sever_driven_ui

enum class SduiViewType{
    PIECHART,
    GRAPH,
    TEXT,
    UNKNOWN;

    companion object {
        fun getViewTypeByOrdinal(ordinalNum: Int): SduiViewType {
            return values()[ordinalNum]
        }
    }
}