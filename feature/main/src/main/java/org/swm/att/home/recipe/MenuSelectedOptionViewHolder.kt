package org.swm.att.home.recipe

import androidx.core.view.children
import androidx.core.view.doOnAttach
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.findViewTreeLifecycleOwner
import com.google.android.material.chip.Chip
import org.swm.att.common_ui.R
import org.swm.att.common_ui.presenter.base.BaseInteractiveViewHolder
import org.swm.att.domain.entity.response.BaseRecyclerViewItem
import org.swm.att.domain.entity.response.OptionVO
import org.swm.att.home.databinding.ItemMenuOptionBinding

class MenuSelectedOptionViewHolder(
    private val binding: ItemMenuOptionBinding,
    private val menuRecipeViewModel: RecipeViewModel
) : BaseInteractiveViewHolder(binding, menuRecipeViewModel) {
    private lateinit var lifecycleOwner: LifecycleOwner

    init {
        itemView.doOnAttach {
            lifecycleOwner = it.findViewTreeLifecycleOwner()!!
            setObserver()
        }
    }

    override fun bind(item: BaseRecyclerViewItem, position: Int?) {
        binding.option = item as OptionVO
        setChipGroup(item)
    }

    private fun setChipGroup(menuOption: OptionVO) {
        binding.cgMenuOptionType.removeAllViews()
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
                    isChecked = type.isSelectable
                    isEnabled = false
                }
                binding.cgMenuOptionType.addView(chip)
            }
        }
    }

    private fun setObserver() {
        menuRecipeViewModel.isModify.observe(lifecycleOwner) {
            binding.cgMenuOptionType.children.iterator().forEach { chip ->
                chip.isEnabled = it
            }
        }
    }
}