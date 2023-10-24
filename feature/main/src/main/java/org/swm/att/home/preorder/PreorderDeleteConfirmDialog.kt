package org.swm.att.home.preorder

import org.swm.att.common_ui.presenter.base.BaseConfirmDialog

class PreorderDeleteConfirmDialog(
    private val preorderViewModel: PreorderViewModel
): BaseConfirmDialog(org.swm.att.common_ui.R.string.tv_confirm_preorder_delete_text) {
    override fun deleteItem() {
        preorderViewModel.deletePreorder()
    }
}