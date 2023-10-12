package org.swm.att.home.recipe

import android.widget.ArrayAdapter
import androidx.core.view.doOnAttach
import androidx.core.widget.addTextChangedListener
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
            binding.recipeViewModel = menuRecipeViewModel
            lifecycleOwner = it.findViewTreeLifecycleOwner()!!
        }
    }

    override fun bind(item: BaseRecyclerViewItem, position: Int?) {
        binding.apply {
            recipeVO = item as RecipeVO
            recipeViewModel = menuRecipeViewModel
        }
        initUnitMenu()
        setBtnDeleteRecipeClickListener(position)
        setEdtTextChangeListener(position)
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

    private fun setBtnDeleteRecipeClickListener(position: Int?) {
        binding.btnDeleteRecipe.setOnClickListener {
            menuRecipeViewModel.deleteRecipeByPosition(position)
        }
    }

    private fun setEdtTextChangeListener(position: Int?) {
        position?.let {
            binding.etRecipeAmount.addTextChangedListener {
                menuRecipeViewModel.recipeListForNewMenu.value?.get(position)?.amount =
                    it.toString()
            }
            binding.etRecipeName.addTextChangedListener {
                menuRecipeViewModel.recipeListForNewMenu.value?.get(position)?.name = it.toString()
            }
            binding.actMenuUnit.addTextChangedListener {
                menuRecipeViewModel.recipeListForNewMenu.value?.get(position)?.unit = it.toString()
            }
        }
    }
}