package org.swm.att.home.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import org.swm.att.common_ui.presenter.base.BaseListAdapter
import org.swm.att.common_ui.util.ItemDiffCallback
import org.swm.att.domain.entity.request.OrderedMenuVO
import org.swm.att.home.databinding.ItemOrderedMenuBinding

class OrderedMenuAdapter: BaseListAdapter<OrderedMenuVO, OrderedMenuViewHolder>(
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
    }
}

class OrderedMenuViewHolder(
    private val binding: ItemOrderedMenuBinding
): RecyclerView.ViewHolder(binding.root) {

    fun bind(orderedMenu: OrderedMenuVO) {
        binding.orderedMenu = orderedMenu
    }
}