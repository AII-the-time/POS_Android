package org.swm.att.home.stock

import org.swm.att.common_ui.presenter.base.BaseDialogFiltering
import java.util.Date

class DialogInventoryDatePicker(
    private val stockViewModel: StockViewModel
): BaseDialogFiltering() {
    override fun onFilteringBtnClick(startDate: Date) {
        stockViewModel.setLastInventoryDate(startDate)
    }
}