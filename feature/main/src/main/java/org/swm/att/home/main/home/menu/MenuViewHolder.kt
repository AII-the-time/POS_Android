package org.swm.att.home.main.home.menu

import androidx.recyclerview.widget.RecyclerView
import org.swm.att.domain.entity.response.BaseRecyclerViewItem
import org.swm.att.domain.entity.response.MenuVO
import org.swm.att.home.databinding.ItemMenuBinding

class MenuViewHolder(
    private val binding: ItemMenuBinding
): RecyclerView.ViewHolder(binding.root){
    fun bind(item: BaseRecyclerViewItem) {
        binding.menu = item as MenuVO
    }
}