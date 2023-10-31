package org.swm.att.home.stock

import org.swm.att.common_ui.presenter.base.BaseConfirmDialog
import org.swm.att.common_ui.R

class DialogStockDeleteConfirm(
    private val stockViewModel: StockViewModel
): BaseConfirmDialog(R.string.tv_confirm_stock_delete_text) {
    override fun deleteItem() {
        stockViewModel.deleteStock()
    }
}