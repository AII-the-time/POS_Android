package org.swm.att.common_ui.viewholder

import android.util.Log
import org.swm.att.common_ui.databinding.ItemSduiUnknownBinding
import org.swm.att.domain.sever_driven_ui.response.SduiReportContent

class SduiUnknownViewHolder(
    private val binding: ItemSduiUnknownBinding
): BaseSduiViewHolder(binding) {
    override fun bind(reportContent: SduiReportContent) {
        Log.d("SduiUnknownViewHolder", "bind: $reportContent")
    }
}