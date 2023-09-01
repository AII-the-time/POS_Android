package org.swm.att.home.home.option

import androidx.core.view.get
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import org.swm.att.common_ui.R
import org.swm.att.domain.entity.response.OptionTypeVO
import org.swm.att.domain.entity.response.OptionVO
import org.swm.att.home.databinding.ItemMenuOptionBinding

class MenuOptionViewHolder(
    private val binding: ItemMenuOptionBinding,
    private val menuOptionViewModel: MenuOptionViewModel
): RecyclerView.ViewHolder(binding.root) {

    fun bind(menuOption: OptionVO) {
        binding.option = menuOption
        binding.cgMenuOptionType.tag = menuOption.id
        setChipGroup(menuOption)
        setChipGroupClickListener()
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
                }
                binding.cgMenuOptionType.addView(chip)
            }
        }
    }

    private fun setChipGroupClickListener() {
        binding.cgMenuOptionType.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId != -1) {
                val optionType = group[checkedId].tag as OptionTypeVO
                menuOptionViewModel.changeSelectedOption(
                    binding.cgMenuOptionType.tag.toString(),
                    optionType
                )
            } else {
                menuOptionViewModel.removeOption(binding.cgMenuOptionType.tag.toString())
            }
        }
    }
}