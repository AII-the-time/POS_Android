package org.swm.att.common_ui.presenter.base

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import org.swm.att.common_ui.R
import org.swm.att.common_ui.databinding.BaseDialogFilteringBinding
import org.swm.att.common_ui.presenter.calendar.CustomCalendarViewModel
import java.util.Date

abstract class BaseDialogFiltering : BaseDialog<BaseDialogFilteringBinding>(R.layout.base_dialog_filtering) {
    private val customCalendarViewModel by viewModels<CustomCalendarViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setDialogClosedBtnClickListener()
        initCustomCalendar()
        setBtnFilteringSearchClickListener()
    }

    private fun setDialogClosedBtnClickListener() {
        binding.btnCloseFilteringDialog.setOnClickListener {
            dismiss()
        }
    }

    private fun initCustomCalendar() {
        binding.customCalendar.initCustomCalendarViewModel(customCalendarViewModel)
    }

    abstract fun onFilteringBtnClick(startDate: Date)


    private fun setBtnFilteringSearchClickListener() {
        binding.btnFilteringSearch.setOnClickListener {
            dismiss()
            val startDate = customCalendarViewModel.startDate.value
            if (startDate != null) {
                onFilteringBtnClick(startDate)
            }
        }
    }
}