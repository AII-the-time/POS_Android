package org.swm.att.home.main.recipe

import android.os.Bundle
import android.view.View
import org.swm.att.common_ui.presenter.base.BaseDialog
import org.swm.att.home.R
import org.swm.att.home.databinding.DialogAddCategoryBinding

class AddCategoryDialog(
    private val recipeViewModel: RecipeViewModel
) : BaseDialog<DialogAddCategoryBinding>(R.layout.dialog_add_category) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBtnClickListener()
    }

    private fun setBtnClickListener() {
        binding.btnCloseAddCategoryDialog.setOnClickListener {
            dismiss()
        }

        binding.btnAddCategory.setOnClickListener {
            val categoryName = binding.edtCategoryName.text.toString()
            if (categoryName.isNotEmpty()) {
                recipeViewModel.postCategory(categoryName)
                dismiss()
            }
        }
    }
}