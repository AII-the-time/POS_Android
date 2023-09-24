package org.swm.att.home.recipe

import com.google.android.material.chip.Chip
import org.swm.att.common_ui.R
import org.swm.att.common_ui.base.BaseInteractiveViewHolder
import org.swm.att.domain.entity.response.BaseRecyclerViewItem
import org.swm.att.domain.entity.response.OptionVO
import org.swm.att.home.databinding.ItemMenuOptionBinding

class MenuSelectedOptionViewHolder(
    private val binding: ItemMenuOptionBinding,
    private val menuRecipeViewModel: RecipeViewModel
) : BaseInteractiveViewHolder(binding, menuRecipeViewModel) {
    override fun bind(item: BaseRecyclerViewItem) {
        binding.option = item as OptionVO
        setChipGroup(item)
    }

    private fun setChipGroup(menuOption: OptionVO) {
        menuOption.options.let {
            for (index in it.indices) {
                val type = it[index]
                val chip = Chip(binding.root.context).apply {
                    tag = type
                    id = index
                    text = if (type.price > 0) {
                        context.getString(
                            R.string.tv_menu_option_type_with_price,
                            type.name,
                            type.price
                        )
                    } else {
                        context.getString(R.string.tv_menu_option_type_with_no_price, type.name)
                    }
                    isChecked = true
                    isEnabled = false
                }
                binding.cgMenuOptionType.addView(chip)
            }
        }
    }
}