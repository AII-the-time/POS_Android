package org.swm.att.common_ui.util.calendar

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import org.swm.att.common_ui.R
import org.swm.att.common_ui.base.BaseListAdapter
import org.swm.att.common_ui.databinding.CustomCalendarDateBinding
import org.swm.att.common_ui.util.Formatter.isToday
import org.swm.att.common_ui.util.ItemDiffCallback
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CustomCalendarAdapter(
    private val customCalendarViewModel: CustomCalendarViewModel,
    private val lifecycleOwner: LifecycleOwner,
    private val isDatePicker: Boolean = false
): BaseListAdapter<Date, CustomCalendarDateViewHolder> (
    ItemDiffCallback(
        onItemsTheSame = { old, new -> old == new },
        onContentTheSame = { old, new -> old == new }
    )
) {
    private val baseDateFormat = SimpleDateFormat("dd", Locale.KOREA)

    override fun inflateBinding(inflater: LayoutInflater, parent: ViewGroup): ViewDataBinding {
        return CustomCalendarDateBinding.inflate(inflater, parent, false)
    }

    override fun createViewHolder(binding: ViewDataBinding): CustomCalendarDateViewHolder {
        return CustomCalendarDateViewHolder(binding as CustomCalendarDateBinding, customCalendarViewModel, lifecycleOwner)
    }

    override fun bindViewHolder(holder: CustomCalendarDateViewHolder, item: Date) {
        holder.bind(item, baseDateFormat.format(item))
        holder.itemView.apply {
            setOnClickListener {
                if (customCalendarViewModel.startDate.value == null) {
                    customCalendarViewModel.setStartDate(item)
                } else {
                    if (!isDatePicker && customCalendarViewModel.endDate.value == null) {
                        if (customCalendarViewModel.startDate.value!! >= item) {
                            customCalendarViewModel.setStartDate(item)
                        } else {
                            customCalendarViewModel.setEndDate(item)
                        }
                    } else {
                        customCalendarViewModel.setStartDate(item)
                    }
                }
            }
        }
    }
}

class CustomCalendarDateViewHolder(
    private val binding: CustomCalendarDateBinding,
    private val viewModel: CustomCalendarViewModel,
    private val lifecycleOwner: LifecycleOwner
): ViewHolder(binding.root) {
    @SuppressLint("ResourceAsColor")
    fun bind(date: Date, strDate: String) {
        binding.date = strDate
        if (isToday(date)) {
            binding.tvCalendarDate.setTextColor(R.color.white)
        }
        setSelectingDateObserver(date)
    }

    private fun setSelectingDateObserver(date: Date) {
        viewModel.startDate.observe(lifecycleOwner) {
            changeBackground(date)
        }
        viewModel.endDate.observe(lifecycleOwner) {
            changeBackground(date)
        }
    }

    @SuppressLint("ResourceAsColor")
    private fun changeBackground(date: Date) {
        val background = if(viewModel.endDate.value == null) {
            if (viewModel.startDate.value == date) {
                R.drawable.shape_oval_date
            } else {
                R.color.white
            }
        } else {
            if (viewModel.startDate.value == date) {
                R.drawable.shape_round_start_date
            } else if (viewModel.endDate.value == date) {
                R.drawable.shape_round_end_date
            } else if (date in viewModel.startDate.value!!..viewModel.endDate.value!!) {
                R.color.main_trans
            } else {
                R.color.white
            }
        }
        binding.tvCalendarDate.setBackgroundResource(background)
    }
}