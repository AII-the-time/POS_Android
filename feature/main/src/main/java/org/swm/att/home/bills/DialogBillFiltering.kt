package org.swm.att.home.bills

import org.swm.att.common_ui.presenter.base.BaseDialogFiltering
import java.util.Date

class DialogBillFiltering(
    private val billViewModel: BillViewModel
): BaseDialogFiltering() {
    override fun onFilteringBtnClick(startDate: Date, endDate: Date?) {
        billViewModel.getBillsForFilteringDates(startDate)
    }
}