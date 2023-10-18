package org.swm.att.common_ui.presenter.base

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import org.swm.att.domain.entity.response.BaseRecyclerViewItem

abstract class BaseRecyclerViewViewHolder(
    binding: ViewDataBinding
): RecyclerView.ViewHolder(binding.root) {
    abstract fun bind(item: BaseRecyclerViewItem)
}