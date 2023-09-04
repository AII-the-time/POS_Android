package org.swm.att.home.bills

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import org.swm.att.common_ui.base.BaseDialog
import org.swm.att.common_ui.util.calendar.CustomCalendarViewModel
import org.swm.att.home.R
import org.swm.att.home.databinding.DialogBillFilteringBinding

class DialogBillFiltering : BaseDialog<DialogBillFilteringBinding>(R.layout.dialog_bill_filtering) {
    private val customCalendarViewModel by viewModels<CustomCalendarViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setDialogClosedBtnClickListener()
        initCustomCalendar()
    }

    private fun setDialogClosedBtnClickListener() {
        binding.btnCloseBillFilteringDialog.setOnClickListener {
            dismiss()
        }
    }

    private fun initCustomCalendar() {
        binding.customCalendar.initCustomCalendarViewModel(customCalendarViewModel)
    }
}