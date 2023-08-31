package org.swm.att.home.home.option

import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import org.swm.att.common_ui.R
import org.swm.att.domain.entity.response.OptionVO
import org.swm.att.home.databinding.ItemMenuOptionBinding

class MenuOptionViewHolder(
    private val binding: ItemMenuOptionBinding,
    private val menuOptionViewModel: MenuOptionViewModel
): RecyclerView.ViewHolder(binding.root) {

    fun bind(menuOption: OptionVO) {
        binding.option= menuOption
        setChipGroup(menuOption)
    }

    private fun setChipGroup(menuOption: OptionVO) {
        menuOption.options?.let {
            for (type in it) {
                val chip = Chip(binding.root.context).apply {
                    id = type.id
                    text = if (type.price > 0) {
                        context.getString(R.string.tv_menu_option_type_with_price, type.name, type.price)
                    } else {
                        context.getString(R.string.tv_menu_option_type_with_no_price, type.name)
                    }
                    setOnCheckedChangeListener { _, b ->
                        val optionVO = OptionVO(
                            id = menuOption.id,
                            optionType = menuOption.optionType,
                            options = listOf(type)
                        )
                        if (b) {
                            menuOptionViewModel.addSelectedOption(optionVO)
                        } else {
                            menuOptionViewModel.removeSelectedOption(optionVO)
                        }
                    }

                }
                binding.cgMenuOptionType.addView(chip)
            }
        }
    }
}