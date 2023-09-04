package org.swm.att.home.bills

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import org.swm.att.common_ui.base.BaseDialog
import org.swm.att.common_ui.util.calendar.CustomCalendarViewModel
import org.swm.att.home.R
import org.swm.att.home.databinding.DialogBillFilteringBinding

class DialogBillFiltering(
    private val billViewModel: BillViewModel
) : BaseDialog<DialogBillFilteringBinding>(R.layout.dialog_bill_filtering) {
    private val customCalendarViewModel by viewModels<CustomCalendarViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setDialogClosedBtnClickListener()
        initCustomCalendar()
        setBtnBillFilteringSearchClickListener()
    }

    private fun setDialogClosedBtnClickListener() {
        binding.btnCloseBillFilteringDialog.setOnClickListener {
            dismiss()
        }
    }

    private fun initCustomCalendar() {
        binding.customCalendar.initCustomCalendarViewModel(customCalendarViewModel)
    }

    private fun setBtnBillFilteringSearchClickListener() {
        binding.btnBillFilteringSearch.setOnClickListener {
            dismiss()
            val startDate = customCalendarViewModel.startDate.value
            val endDate = customCalendarViewModel.endDate.value
            if (startDate != null) {
                billViewModel.getBillsForFilteringDates(startDate, endDate)
            }
        }
    }
}