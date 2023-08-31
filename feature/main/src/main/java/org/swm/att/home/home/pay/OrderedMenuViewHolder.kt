package org.swm.att.home.home.pay

import androidx.recyclerview.widget.RecyclerView
import org.swm.att.domain.entity.request.OrderedMenuVO
import org.swm.att.home.databinding.ItemOrderedMenuBinding

class OrderedMenuViewHolder(
    private val binding: ItemOrderedMenuBinding
): RecyclerView.ViewHolder(binding.root) {

    fun bind(orderedMenu: OrderedMenuVO) {
        binding.orderedMenu = orderedMenu
    }
}