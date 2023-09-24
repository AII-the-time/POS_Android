package org.swm.att.home.recipe

import android.widget.ArrayAdapter
import org.swm.att.common_ui.R
import org.swm.att.common_ui.base.BaseInteractiveViewHolder
import org.swm.att.domain.entity.response.BaseRecyclerViewItem
import org.swm.att.domain.entity.response.RecipeVO
import org.swm.att.home.databinding.ItemMenuRecipeBinding

class MenuRecipeViewHolder(
    private val binding: ItemMenuRecipeBinding,
    private val menuRecipeViewModel: RecipeViewModel
) : BaseInteractiveViewHolder(binding, menuRecipeViewModel) {
    override fun bind(item: BaseRecyclerViewItem) {
        binding.recipeVO = item as RecipeVO
        initUnitMenu()
    }

    private fun initUnitMenu() {
        val unitArray = binding.root.context.resources.getStringArray(R.array.recipe_unit)
        val arrayAdapter =
            ArrayAdapter(binding.root.context, org.swm.att.home.R.layout.item_menu_unit, unitArray)
        binding.actMenuUnit.apply {
            setAdapter(arrayAdapter)
            // 단위 'g'을 default로 설정
            setText(unitArray[0], false)
        }
    }
}