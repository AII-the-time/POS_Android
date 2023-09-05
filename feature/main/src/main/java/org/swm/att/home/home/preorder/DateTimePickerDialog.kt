package org.swm.att.home.home.preorder

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import org.swm.att.common_ui.base.BaseDialog
import org.swm.att.common_ui.util.calendar.CustomCalendarViewModel
import org.swm.att.home.R
import org.swm.att.home.databinding.DialogDateTimePickerBinding

class DateTimePickerDialog : BaseDialog<DialogDateTimePickerBinding>(R.layout.dialog_date_time_picker) {
    private val customCalendarViewModel by viewModels<CustomCalendarViewModel>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setDialogCloseBtnClickListener()
        initCustomCalendar()
        initNumberPicker()
    }

    private fun setDialogCloseBtnClickListener() {
        binding.btnCloseDateTimePickerDialog.setOnClickListener {
            dismiss()
        }
    }

    private fun initCustomCalendar() {
        binding.customCalendar.initDatePickerCustomCalendarViewModel(customCalendarViewModel)
    }

    private fun initNumberPicker() {
        binding.npHourPicker.apply {
            maxValue = 24
            setOnValueChangedListener { _, _, _ ->

            }
        }

        binding.npMinutePicker.apply {
            maxValue = 59
            setOnValueChangedListener { _, _, _ ->

            }
        }
    }


}