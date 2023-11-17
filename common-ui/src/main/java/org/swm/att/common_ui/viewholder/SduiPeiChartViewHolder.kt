package org.swm.att.common_ui.viewholder

import android.util.Log
import org.swm.att.common_ui.databinding.ItemSduiPiechartBinding
import org.swm.att.domain.sever_driven_ui.response.SduiReportContent


class SduiPeiChartViewHolder(
    private val binding: ItemSduiPiechartBinding
): BaseSduiViewHolder(binding) {
    override fun bind(reportContent: SduiReportContent) {
        Log.d("PieChartViewHolder", "bind: $reportContent")
    }
}