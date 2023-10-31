package org.swm.att.home.main.bills

import org.swm.att.common_ui.presenter.base.BaseConfirmDialog
import org.swm.att.common_ui.R

class BillCancelConfirmDialog(
    private val billViewModel: BillViewModel
): BaseConfirmDialog(R.string.tv_confirm_bill_cancel_text) {
    override fun deleteItem() {
        billViewModel.cancelOrder()
    }
}