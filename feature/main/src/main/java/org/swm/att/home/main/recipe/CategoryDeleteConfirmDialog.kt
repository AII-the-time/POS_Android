package org.swm.att.home.main.recipe

import org.swm.att.common_ui.presenter.base.BaseConfirmDialog
import org.swm.att.common_ui.R

class CategoryDeleteConfirmDialog (
    private val recipeViewModel: RecipeViewModel
): BaseConfirmDialog(R.string.tv_confirm_category_delete_text) {
    override fun deleteItem() {
        recipeViewModel.deleteCategory()
    }
}