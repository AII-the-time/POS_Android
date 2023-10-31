package org.swm.att.home.main.recipe

import org.swm.att.common_ui.presenter.base.BaseConfirmDialog
import org.swm.att.common_ui.R

class MenuDeleteConfirmDialog(
    private val recipeViewModel: RecipeViewModel
): BaseConfirmDialog(R.string.tv_confirm_menu_delete_text) {
    override fun deleteItem() {
        recipeViewModel.deleteMenu()
    }
}