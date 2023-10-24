package org.swm.att.home.preorder

import org.swm.att.common_ui.presenter.base.BaseConfirmDialog
import org.swm.att.common_ui.R

class DialogPreorderDeleteConfirm(
    private val preorderViewModel: PreorderViewModel
): BaseConfirmDialog(R.string.tv_confirm_preorder_delete_text) {
    override fun deleteItem() {
        preorderViewModel.deletePreorder()
    }
}