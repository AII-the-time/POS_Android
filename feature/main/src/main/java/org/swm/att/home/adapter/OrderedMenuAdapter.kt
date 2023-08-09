package org.swm.att.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ListAdapter
import org.swm.att.common_ui.util.ItemDiffCallback
import org.swm.att.domain.entity.request.OrderedMenuVO
import org.swm.att.home.R
import org.swm.att.home.databinding.ItemOrderedMenuBinding
import org.swm.att.home.pay.OrderedMenuViewHolder
import org.swm.att.home.pay.PayViewModel

class OrderedMenuAdapter(
): ListAdapter<OrderedMenuVO, OrderedMenuViewHolder>(
    ItemDiffCallback<OrderedMenuVO>(
        onItemsTheSame = { old, new -> old.menu.id == new.menu.id },
        onContentTheSame = { old, new -> old == new }
    )
){
    private lateinit var binding: ItemOrderedMenuBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderedMenuViewHolder {
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_ordered_menu, parent, false)
        return OrderedMenuViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderedMenuViewHolder, position: Int) {
        val orderedMenu = getItem(position)
        holder.bind(orderedMenu)
    }
}