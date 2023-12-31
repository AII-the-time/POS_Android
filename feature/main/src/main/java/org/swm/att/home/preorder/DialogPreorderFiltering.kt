package org.swm.att.home.preorder

import org.swm.att.common_ui.presenter.base.BaseDialogFiltering
import java.util.Date

class DialogPreorderFiltering(
    private val preorderViewModel: PreorderViewModel
): BaseDialogFiltering() {
    override fun onFilteringBtnClick(startDate: Date) {
        preorderViewModel.getPreordersForFilteringDates(startDate)
    }
}