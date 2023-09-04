package org.swm.att.common_ui.util.calendar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.swm.att.common_ui.base.BaseViewModel
import java.util.Date

class CustomCalendarViewModel: BaseViewModel() {
    private val _startDate = MutableLiveData<Date?>(null)
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