package org.swm.att.common_ui.viewholder

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import org.swm.att.domain.sever_driven_ui.response.SduiReportContent

sealed class BaseSduiViewHolder(
    binding: ViewDataBinding
): RecyclerView.ViewHolder(binding.root) {
    abstract fun bind(reportContent: SduiReportContent)
}