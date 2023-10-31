package org.swm.att.home.main.home

import androidx.recyclerview.widget.RecyclerView
import org.swm.att.domain.entity.request.OrderedMenuVO
import org.swm.att.home.databinding.ItemSelectedMenuBinding

class SelectedMenuViewHolder(
    private val binding: ItemSelectedMenuBinding,
): RecyclerView.ViewHolder(binding.root) {

    fun bind(menu: OrderedMenuVO, count: Int) {
        binding.menu = menu
        binding.count = count
    }
}