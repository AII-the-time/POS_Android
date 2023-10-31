package org.swm.att.home.main.stock

import org.swm.att.common_ui.presenter.base.BaseDialogFiltering
import java.util.Date

class DialogInventoryDatePicker(
    private val stockViewModel: StockViewModel
): BaseDialogFiltering() {
    override fun onFilteringBtnClick(startDate: Date, endDate: Date?) {
        stockViewModel.setLastInventoryDate(startDate)
    }
}