package org.swm.att.home.home.menu

import org.swm.att.common_ui.base.BaseSelectableViewHolder
import org.swm.att.domain.entity.response.BaseSelectableItem
import org.swm.att.domain.entity.response.MenuVO
import org.swm.att.home.databinding.ItemMenuBinding

class MenuViewHolder(
    private val binding: ItemMenuBinding
): BaseSelectableViewHolder(binding){
    override fun bind(item: BaseSelectableItem) {
        binding.menu = item as MenuVO
    }
}