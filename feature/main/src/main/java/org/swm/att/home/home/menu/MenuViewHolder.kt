package org.swm.att.home.home.menu

import androidx.recyclerview.widget.RecyclerView
import org.swm.att.domain.entity.response.BaseSelectableItem
import org.swm.att.domain.entity.response.MenuVO
import org.swm.att.home.databinding.ItemMenuBinding

class MenuViewHolder(
    private val binding: ItemMenuBinding
): RecyclerView.ViewHolder(binding.root){
    fun bind(item: BaseSelectableItem) {
        binding.menu = item as MenuVO
    }
}