package org.swm.att.home.main.home.option

import androidx.core.view.get
import com.google.android.material.chip.Chip
import org.swm.att.common_ui.R
import org.swm.att.common_ui.presenter.base.BaseInteractiveViewHolder
import org.swm.att.domain.entity.response.BaseRecyclerViewItem
import org.swm.att.domain.entity.response.OptionTypeVO
import org.swm.att.domain.entity.response.OptionVO
import org.swm.att.home.databinding.ItemMenuOptionBinding

class MenuOptionViewHolder(
    private val binding: ItemMenuOptionBinding,
    private val menuOptionViewModel: MenuOptionViewModel
): BaseInteractiveViewHolder(binding, menuOptionViewModel){

    override fun bind(item: BaseRecyclerViewItem, position: Int?) {
        val item = item as OptionVO
        binding.option = item
        binding.cgMenuOptionType.tag = item
        setChipGroup(item)
        setChipGroupClickListener()
    }

    private fun setChipGroup(menuOption: OptionVO) {
        binding.cgMenuOptionType.isSingleSelection = true
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
                    isEnabled = type.isSelectable
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
                    group.tag.toString(),
                    optionType
                )
            } else {
                menuOptionViewModel.removeOption(binding.cgMenuOptionType.tag.toString())
            }
        }
    }
}