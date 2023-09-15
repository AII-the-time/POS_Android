package org.swm.att.common_ui.util.calendar

import android.content.Context
import android.content.ContextWrapper
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.LifecycleOwner
import org.swm.att.common_ui.databinding.CustomCalendarBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class CustomCalendar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
): ConstraintLayout(context, attrs, defStyleAttr) {
    private lateinit var lifecycleOwner: LifecycleOwner
    private lateinit var customCalendarAdapter: CustomCalendarAdapter
    private var month: Int = -1
    private var year: Int = -1
    private var calInstance = Calendar.getInstance()
    private val baseMonthFormatter = SimpleDateFormat("yyyy년 MM월", Locale.KOREA)
    private val binding: CustomCalendarBinding by lazy {
        CustomCalendarBinding.inflate(LayoutInflater.from(context), this, true)
    }
    private lateinit var customCalendarViewModel: CustomCalendarViewModel
    private var dates = ArrayList<Date>()

    init {
        initControl()
        setLifecycleOwner()
    }

    fun initCustomCalendarViewModel(viewModel: CustomCalendarViewModel) {
        customCalendarViewModel = viewModel
        initDatesRecyclerView()
    }

    fun initDatePickerCustomCalendarViewModel(viewModel: CustomCalendarViewModel) {
        customCalendarViewModel = viewModel
        initDatesRecyclerView(true)
    }

    private fun initDatesRecyclerView(isDatePicker: Boolean = false) {
        customCalendarAdapter = if (isDatePicker) {
            CustomCalendarAdapter(customCalendarViewModel, lifecycleOwner, true)
        } else {
            CustomCalendarAdapter(customCalendarViewModel, lifecycleOwner)
        }

        binding.rvCalendarDates.apply {
            setHasFixedSize(true)
            adapter = customCalendarAdapter
            itemAnimator = null
        }
        updateCalendar()
    }

    private fun setLifecycleOwner() {
        lifecycleOwner = if (context is LifecycleOwner) {
            context as LifecycleOwner
        } else {
            (context as ContextWrapper).baseContext as LifecycleOwner
        }
    }

    private fun updateCalendar() {
        binding.currentMonth = baseMonthFormatter.format(calInstance.time)
        dates = ArrayList()
        month = calInstance.get(Calendar.MONTH)
        year = calInstance.get(Calendar.YEAR)

        calInstance.set(Calendar.DAY_OF_MONTH, 1)
        calInstance.apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        val monthBeginningCell = calInstance.get(Calendar.DAY_OF_WEEK) - 1
        calInstance.add(Calendar.DAY_OF_MONTH, -monthBeginningCell)

        while (dates.size < 42) {
            dates.add(calInstance.time)
            calInstance.add(Calendar.DAY_OF_MONTH, 1)
        }
        customCalendarAdapter.submitList(dates)
    }

    private fun initControl() {
        binding.btnCalendarPastMonth.setOnClickListener {
            goToPastMonth()
        }
        binding.btnCalendarNextMonth.setOnClickListener {
            goToNextMonth()
        }
    }

    private fun goToPastMonth() {
        calInstance.set(Calendar.YEAR, year)
        calInstance.set(Calendar.MONTH, month)
        calInstance.add(Calendar.MONTH, -1)
        updateCalendar()
    }

    private fun goToNextMonth() {
        calInstance.set(Calendar.YEAR, year)
        calInstance.set(Calendar.MONTH, month)
        calInstance.add(Calendar.MONTH, 1)
        updateCalendar()
    }

}