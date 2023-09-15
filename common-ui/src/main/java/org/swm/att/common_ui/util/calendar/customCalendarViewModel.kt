package org.swm.att.common_ui.util.calendar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.swm.att.common_ui.base.BaseViewModel
import java.util.Calendar
import java.util.Date

class CustomCalendarViewModel: BaseViewModel() {
    private val _startDate = MutableLiveData(Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0) }.time)
    val startDate: LiveData<Date?> = _startDate
    private val _endDate = MutableLiveData<Date?>(null)
    val endDate: LiveData<Date?> = _endDate

    fun setStartDate(date: Date) {
        _startDate.postValue(date)
        _endDate.postValue(null)
    }

    fun setEndDate(date: Date) {
        _endDate.postValue(date)
    }
}