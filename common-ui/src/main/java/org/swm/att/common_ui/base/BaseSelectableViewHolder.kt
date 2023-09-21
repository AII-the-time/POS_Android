package org.swm.att.common_ui.base

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import org.swm.att.domain.entity.response.BaseSelectableItem

abstract class BaseSelectableViewHolder(
    binding: ViewDataBinding
): RecyclerView.ViewHolder(binding.root) {
    abstract fun bind(item: BaseSelectableItem)
}