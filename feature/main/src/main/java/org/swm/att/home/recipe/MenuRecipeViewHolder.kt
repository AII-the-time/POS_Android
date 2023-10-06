package org.swm.att.home.recipe

import android.widget.ArrayAdapter
import androidx.core.view.doOnAttach
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.findViewTreeLifecycleOwner
import org.swm.att.common_ui.R
import org.swm.att.common_ui.presenter.base.BaseInteractiveViewHolder
import org.swm.att.domain.entity.response.BaseRecyclerViewItem
import org.swm.att.domain.entity.response.RecipeVO
import org.swm.att.home.databinding.ItemMenuRecipeBinding

class MenuRecipeViewHolder(
    private val binding: ItemMenuRecipeBinding,
    private val menuRecipeViewModel: RecipeViewModel
) : BaseInteractiveViewHolder(binding, menuRecipeViewModel) {
    private lateinit var lifecycleOwner: LifecycleOwner

    init {
        itemView.doOnAttach {
            lifecycleOwner = it.findViewTreeLifecycleOwner()!!
            setObserver()
        }
    }

    override fun bind(item: BaseRecyclerViewItem) {
        binding.apply {
            recipeVO = item as RecipeVO
            recipeViewModel = menuRecipeViewModel
        }
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

    private fun setObserver() {
        menuRecipeViewModel.isModify.observe(lifecycleOwner) {
            binding.etRecipeName.isEnabled = it
            binding.etRecipeAmount.isEnabled = it
            if (it) {
                binding.tilRecipeUnit.visibility = android.view.View.VISIBLE
                binding.tvRecipeUnit.visibility = android.view.View.GONE
                binding.btnDeleteRecipe.visibility = android.view.View.VISIBLE
            } else {
                binding.tilRecipeUnit.visibility = android.view.View.GONE
                binding.tvRecipeUnit.visibility = android.view.View.VISIBLE
                binding.btnDeleteRecipe.visibility = android.view.View.GONE
            }
        }
    }
}