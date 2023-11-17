package org.swm.att.common_ui.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import org.swm.att.common_ui.R
import org.swm.att.common_ui.databinding.ItemSduiGraphBinding
import org.swm.att.common_ui.databinding.ItemSduiPiechartBinding
import org.swm.att.common_ui.databinding.ItemSduiUnknownBinding
import org.swm.att.domain.sever_driven_ui.SduiViewType

fun getSduiViewHolder(parent: ViewGroup, viewType: SduiViewType): BaseSduiViewHolder {
    val layout = getLayoutByViewType(viewType)
    val binding: ViewDataBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), layout, parent, false)

    //수정해야 함.
    return when(viewType) {
        SduiViewType.PIECHART -> SduiPieChartViewHolder(binding as ItemSduiPiechartBinding)
        SduiViewType.GRAPH -> SduiGraphViewHolder(binding as ItemSduiGraphBinding)
        SduiViewType.UNKNOWN -> SduiUnknownViewHolder(binding as ItemSduiUnknownBinding)
    }
}

fun getLayoutByViewType(viewType: SduiViewType): Int {
    return when(viewType) {
        SduiViewType.PIECHART -> R.layout.item_sdui_piechart
        SduiViewType.GRAPH -> R.layout.item_sdui_graph
        SduiViewType.UNKNOWN -> R.layout.item_sdui_unknown
    }
}