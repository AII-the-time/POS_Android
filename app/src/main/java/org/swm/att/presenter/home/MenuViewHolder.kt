package org.swm.att.presenter.home

import androidx.recyclerview.widget.RecyclerView
import org.swm.att.databinding.ItemMenuBinding
import org.swm.att.domain.entity.response.MenuVO

class MenuViewHolder(
    private val binding: ItemMenuBinding
): RecyclerView.ViewHolder(binding.root) {

    fun bind(menu: MenuVO) {
        binding.menu = menu
    }
}