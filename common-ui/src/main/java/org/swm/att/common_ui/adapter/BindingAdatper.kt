package org.swm.att.common_ui.adapter

import android.annotation.SuppressLint
import android.os.Build
import android.view.View
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import org.swm.att.common_ui.R
import org.swm.att.common_ui.constant.StockState
import org.swm.att.common_ui.state.UiState
import org.swm.att.common_ui.util.Formatter.getDataTimeBaseFormattingResult
import org.swm.att.common_ui.util.Formatter.getDateBaseFormattingResult
import org.swm.att.common_ui.util.Formatter.getDateFromString
import org.swm.att.common_ui.util.Formatter.getStringBaseCurrencyUnit
import org.swm.att.common_ui.util.Formatter.getTimeFromString
import org.swm.att.common_ui.util.getRTCDateTime
import org.swm.att.domain.entity.request.OrderedMenuVO
import org.swm.att.domain.entity.response.CategoriesVO
import org.swm.att.domain.entity.response.MileageVO
import org.swm.att.domain.entity.response.OptionTypeVO
import org.swm.att.domain.entity.response.StockWithStateVO
import java.util.Date
import java.util.Stack

@BindingAdapter("customPriceText")
fun <T> setCustomPriceText(view: TextView, price: T) {
    if (price is String) {
        view.text =
            view.context.getString(R.string.tv_custom_price_text, getStringBaseCurrencyUnit(price))
    } else if (price is Int) {
        view.text = view.context.getString(
            R.string.tv_custom_price_text,
            getStringBaseCurrencyUnit(price.toString())
        )
    }
}

@BindingAdapter("customLiveDataPriceText")
fun setCustomLiveDataPriceText(view: TextView, price: LiveData<String>) {
    val currency = getStringBaseCurrencyUnit(price.value ?: "0")
    view.text = view.context.getString(R.string.tv_custom_price_text, currency)
}

@BindingAdapter("price", "totalCount")
fun setCustomTotalPriceText(view: TextView, price: Int, totalCount: Int) {
    val totalPrice = getStringBaseCurrencyUnit((price * totalCount).toString())
    view.text = view.context.getString(R.string.tv_custom_price_text, totalPrice)
}

@BindingAdapter("customTotalCountText")
fun setCustomTotalCountText(view: TextView, menuMap: Map<OrderedMenuVO, Int>?) {
    val size = getStringBaseCurrencyUnit((menuMap?.map { it.value }?.sum() ?: 0).toString())
    view.text = view.context.getString(R.string.tv_custom_total_count_text, size)
}

@BindingAdapter("totalPriceMap", "useMileage")
fun setCustomPayPriceText(
    view: TextView,
    totalPriceMap: Map<OrderedMenuVO, Int>?,
    useMileage: Stack<String>?
) {
    val totalPrice = totalPriceMap?.map { it.key.price * it.value }?.sum() ?: 0
    var useMileagePrice = 0
    useMileage?.let {
        if (it.isNotEmpty()) {
            useMileagePrice = useMileage.joinToString("").toInt()
        }
    }
    view.text = view.context.getString(
        R.string.tv_custom_price_text,
        getStringBaseCurrencyUnit((totalPrice - useMileagePrice).toString()))
}

@BindingAdapter("customTotalPriceText")
fun setCustomTotalPriceText(view: TextView, menuMap: Map<OrderedMenuVO, Int>?) {
    val totalPrice = getStringBaseCurrencyUnit((menuMap?.map { it.key.price * it.value }?.sum() ?: 0).toString())
    view.text = view.context.getString(R.string.tv_custom_price_text, totalPrice)
}

@BindingAdapter("saveMileageText")
fun setSaveMileageText(view: TextView, menuMap: Map<OrderedMenuVO, Int>?) {
    val saveMileage = (menuMap?.map { it.key.price * it.value }?.sum() ?: 0) * 0.1
    view.text = getStringBaseCurrencyUnit(saveMileage.toInt().toString())
}

@BindingAdapter("optionListText")
fun setOptionListText(view: TextView, optionList: List<OptionTypeVO>?) {
    val optionStr = optionList?.let { list ->
        list.joinToString { type ->
            type.name
        }
    }
    view.text = optionStr
}

@BindingAdapter("setDetailVisibility")
fun setDetailVisibility(view: TextView, detail: String?) {
    view.visibility = if (detail.isNullOrEmpty()) {
        TextView.GONE
    } else {
        TextView.VISIBLE
    }
}

@BindingAdapter("setOptionsVisibility")
fun setOptionsVisibility(view: TextView, optionList: List<OptionTypeVO>?) {
    view.visibility = if (optionList.isNullOrEmpty()) {
        TextView.GONE
    } else {
        TextView.VISIBLE
    }
}

@BindingAdapter("setCustomerNumber")
fun setCustomerNumber(view: TextView, phoneNumber: String?) {
    phoneNumber?.let {
        val number = phoneNumber.takeLast(4)
        view.text = view.context.getString(R.string.tv_customer_number, number)
    }
}

@BindingAdapter("setCustomerMileage")
fun setCustomerMileage(view: TextView, mileage: Int) {
    val mileageStr = getStringBaseCurrencyUnit(mileage.toString())
    view.text = view.context.getString(R.string.tv_mileage_text, mileageStr)
}

@BindingAdapter("customClickable")
fun setCustomClickable(view: TextView, mileageVO: MileageVO?) {
    mileageVO?.let {
        view.isClickable = it.mileage > 0
    }
}

@BindingAdapter("setCustomStrMileage")
fun setCustomStrMileage(view: TextView, stack: Stack<String>?){
    var mileage = "0"
    stack?.let {
        if (it.isNotEmpty()) {
            mileage = getStringBaseCurrencyUnit(stack.joinToString(""))
        }
    }
    view.text = view.context.getString(R.string.tv_mileage_text, mileage)
}

@BindingAdapter("setCustomUseAllMileage")
fun setCustomUseAllMileage(view: TextView, mileage: Int) {
    val mileageStr = getStringBaseCurrencyUnit(mileage.toString())
    view.text = view.context.getString(R.string.tv_use_all_mileage, mileageStr)
    view.paint.isUnderlineText = true
}

@BindingAdapter("totalMileage", "useMileage")
fun setCustomMileageStrColor(view: TextView, totalMileage: Int, useMileage: Stack<String>?) {
    var color = R.color.black
    useMileage?.let {
        if (it.isNotEmpty()) {
            color = if (totalMileage < (useMileage.joinToString("").toInt())) {
                R.color.key_pad_gray
            } else {
                R.color.black
            }
        }
    }
    view.setTextColor(view.context.getColor(color))
}

@BindingAdapter("setOrderBtnClickable")
fun setOrderBtnClickable(view: AppCompatButton, selectedMenuCount: Int?) {
    selectedMenuCount?.let {
        view.isClickable = selectedMenuCount > 0
    }
}

@BindingAdapter("startDateText")
fun setStartDateText(view: TextView, startDateText: Date?) {
    view.text = if (startDateText == null) {
        view.context.getString(R.string.tv_no_filtering)
    } else {
        view.context.getString(R.string.tv_filtering_start, getDateBaseFormattingResult(startDateText))
    }
}
@BindingAdapter("endDateText")
fun setEndDateText(view: TextView, endDateText: Date?) {
    view.text = if (endDateText != null) {
        view.context.getString(R.string.tv_filtering_end, getDateBaseFormattingResult(endDateText))
    } else {
        null
    }
}

@RequiresApi(Build.VERSION_CODES.Q)
@BindingAdapter("setEditTextStyleByIsModify")
fun setEditTextStyleByIsModify(view: AppCompatEditText, isModify: Boolean) {
    view.isEnabled = isModify
    view.isFocusable = isModify
    view.isFocusableInTouchMode = isModify
}

@BindingAdapter("localDateTimeText")
fun setLocalDateTimeText(view: TextView, date: String?) {
    date?.let {
        val localTime = getDateFromString(it).getRTCDateTime()
        view.text = getDataTimeBaseFormattingResult(localTime)
    }
}

@BindingAdapter("localTimeText")
fun setLocalTimeText(view: TextView, date: String?) {
    date?.let {
        val localTime = getDateFromString(it).getRTCDateTime()
        view.text = getTimeFromString(localTime)
    }
}

@BindingAdapter("localDateText")
fun setLocalDateText(view: TextView, date: String?) {
    date?.let {
        val localTime = getDateFromString(it).getRTCDateTime()
        view.text = getDateBaseFormattingResult(localTime)
    }
}

@SuppressLint("UseCompatLoadingForDrawables")
@BindingAdapter("setSelectableItemBackground")
fun setSelectableItemBackground(view: ConstraintLayout, isFocused: Boolean) {
    view.background =
        if (isFocused) view.context.getDrawable(R.color.main_trans) else view.context.getDrawable(R.color.back_color)
}

@BindingAdapter("visibilityByUiState")
fun setVisibilityByUiState(view: TextView, uiState: UiState<CategoriesVO>) {
    when (uiState) {
        is UiState.Success -> {
            view.visibility = if (uiState.data?.categories.isNullOrEmpty()) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }
        else -> {
            view.visibility = View.VISIBLE
        }
    }
}

@BindingAdapter("backgroundByStockState")
fun setBackgroundByStockState(view: AppCompatImageView, stockWithStateVO: StockWithStateVO) {
    val stockState = StockState.toStockState(stockWithStateVO.state)
    if (stockState.icon == null) {
        view.visibility = View.GONE
    } else {
        view.background = view.context.getDrawable(stockState.icon)
        view.visibility = View.VISIBLE
    }
}

@BindingAdapter("textByStockState")
fun setTextByStockState(view: TextView, stockWithStateVO: StockWithStateVO) {
    val stockState = StockState.toStockState(stockWithStateVO.state)
    view.text = stockState.state
}