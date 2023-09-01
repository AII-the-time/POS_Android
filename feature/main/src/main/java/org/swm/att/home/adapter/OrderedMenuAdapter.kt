package org.swm.att.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ListAdapter
import org.swm.att.common_ui.util.ItemDiffCallback
import org.swm.att.domain.entity.request.OrderedMenuVO
import org.swm.att.home.R
import org.swm.att.home.databinding.ItemOrderedMenuBinding
import org.swm.att.home.home.pay.OrderedMenuViewHolder
import org.swm.att.home.home.pay.PayViewModel

class OrderedMenuAdapter(
    private val payViewModel: PayViewModel,
    private val selected: Boolean
): ListAdapter<OrderedMenuVO, OrderedMenuViewHolder>(
    ItemDiffCallback<OrderedMenuVO>(
        onItemsTheSame = { old, new -> old.id == new.id && old.options == new.options },
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

        holder.itemView.setOnClickListener {
            if (selected) {
                payViewModel.moveSelectedMenuToOrderedList(orderedMenu)
            } else {
                payViewModel.moveOrderedMenuToSelectedList(orderedMenu)
            }
        }

    }
}