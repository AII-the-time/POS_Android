package org.swm.att.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import org.swm.att.common_ui.base.BaseListAdapter
import org.swm.att.common_ui.util.ItemDiffCallback
import org.swm.att.domain.entity.request.OrderedMenuVO
import org.swm.att.home.databinding.ItemOrderedMenuBinding
import org.swm.att.home.home.pay.OrderedMenuViewHolder
import org.swm.att.home.home.pay.PayViewModel

class OrderedMenuAdapter(
    private val payViewModel: PayViewModel,
    private val selected: Boolean
): BaseListAdapter<OrderedMenuVO, OrderedMenuViewHolder>(
    ItemDiffCallback(
        onItemsTheSame = { old, new -> old.id == new.id && old.options == new.options },
        onContentTheSame = { old, new -> old == new }
    )
) {
    override fun inflateBinding(inflater: LayoutInflater, parent: ViewGroup): ViewDataBinding {
        return ItemOrderedMenuBinding.inflate(inflater, parent, false)
    }

    override fun createViewHolder(binding: ViewDataBinding): OrderedMenuViewHolder {
        return OrderedMenuViewHolder(binding as ItemOrderedMenuBinding)
    }

    override fun bindViewHolder(holder: OrderedMenuViewHolder, item: OrderedMenuVO) {
        holder.bind(item)
        holder.itemView.setOnClickListener {
            if (selected) {
                payViewModel.moveSelectedMenuToOrderedList(item)
            } else {
                payViewModel.moveOrderedMenuToSelectedList(item)
            }
        }
    }
}