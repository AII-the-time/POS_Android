package org.swm.att.common_ui.viewholder

import android.util.Log
import org.swm.att.common_ui.databinding.ItemSduiGraphBinding
import org.swm.att.domain.sever_driven_ui.response.SduiReportContent

class SduiGraphViewHolder(
    private val binding: ItemSduiGraphBinding
): BaseSduiViewHolder(binding) {
    override fun bind(reportContent: SduiReportContent) {
        Log.d("GraphViewHolder", "bind: $reportContent")
    }
}