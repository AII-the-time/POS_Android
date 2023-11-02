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
    private val menuRecipeViewModel: RecipeViewModel,
) : BaseInteractiveViewHolder(binding, menuRecipeViewModel) {
    private var owner: LifecycleOwner? = null
    private var recipeId: Int? = null

    init {
        itemView.doOnAttach {
            owner = it.findViewTreeLifecycleOwner()
            binding.lifecycleOwner = owner
        }
    }

    override fun bind(item: BaseRecyclerViewItem, position: Int?) {
        val recipe = item as RecipeVO
        recipeId = recipe.id
        binding.apply {
            recipeVO = recipe
            recipeViewModel = menuRecipeViewModel
        }
        initUnitMenu(recipe.unit)
        setBtnDeleteRecipeClickListener(recipe.id)
    }

    private fun initUnitMenu(unit: String) {
        val unitArray = binding.root.context.resources.getStringArray(R.array.recipe_unit)
        val arrayAdapter =
            ArrayAdapter(binding.root.context, org.swm.att.home.R.layout.item_simple_text, unitArray)
        binding.actMenuUnit.apply {
            setAdapter(arrayAdapter)
//            setText(unit, false)
        }
    }

    private fun setBtnDeleteRecipeClickListener(stockId: Int) {
        binding.btnDeleteRecipe.setOnClickListener {
            menuRecipeViewModel.deleteRecipeByPosition(stockId)
        }
    }

    fun getRecipe(): RecipeVO {
        return RecipeVO(
            id = recipeId ?: -1,
            name = binding.etRecipeName.text.toString(),
            isMixed = false,
            coldRegularAmount = binding.etRecipeAmount.text.toString(),
            unit = binding.actMenuUnit.text.toString(),
        )
    }
}