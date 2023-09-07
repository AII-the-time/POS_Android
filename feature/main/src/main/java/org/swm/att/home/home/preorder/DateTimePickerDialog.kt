package org.swm.att.home.home.preorder

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import org.swm.att.common_ui.base.BaseDialog
import org.swm.att.common_ui.util.calendar.CustomCalendarViewModel
import org.swm.att.home.R
import org.swm.att.home.databinding.DialogDateTimePickerBinding
import org.swm.att.home.home.HomeViewModel
import java.util.Calendar

class DateTimePickerDialog(
    private val homeViewModel: HomeViewModel
) : BaseDialog<DialogDateTimePickerBinding>(R.layout.dialog_date_time_picker) {
    private val customCalendarViewModel by viewModels<CustomCalendarViewModel>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setDialogCloseBtnClickListener()
        initCustomCalendar()
        initNumberPicker()
        setProcessPreorderBtnClickListener()
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
        }
        binding.npMinutePicker.apply {
            maxValue = 59
        }
    }

    private fun setProcessPreorderBtnClickListener() {
        binding.btnProcessPreorder.setOnClickListener {
            customCalendarViewModel.startDate.value?.let {
                val calendar = Calendar.getInstance()
                val cur = calendar.time
                calendar.time = it
                calendar.set(Calendar.HOUR_OF_DAY, binding.npHourPicker.value)
                calendar.set(Calendar.MINUTE, binding.npMinutePicker.value)
                if (calendar.time < cur) {
                    Toast.makeText(context, "현재 시간 이후로 선택해주세요.", Toast.LENGTH_SHORT).show()
                } else {
                    dismiss()
                    homeViewModel.setPreorderDateTime(calendar.time)
                    val inputUserPhoneNumDialog = PreorderInputUserPhoneNumDialog(homeViewModel)
                    inputUserPhoneNumDialog.show(parentFragmentManager, "inputUserPhoneNumDialog")
                }
            }
        }
    }
}