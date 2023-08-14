package org.swm.att.common_ui.util

import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.databinding.BindingAdapter
import org.swm.att.common_ui.R
import org.swm.att.common_ui.util.CurrencyFormat.getUnit
import org.swm.att.domain.entity.response.MenuVO
import org.swm.att.domain.entity.response.MileageVO
import org.swm.att.domain.entity.response.OptionVO
import java.util.Stack

@BindingAdapter("customPriceText")
fun setCustomPriceText(view: TextView, price: Int) {
    val currency = price.toString().getUnit()
    view.text = view.context.getString(R.string.tv_custom_price_text, currency)
}

@BindingAdapter("price", "totalCount")
fun setCustomTotalPriceText(view: TextView, price: Int, totalCount: Int) {
    val totalPrice = (price * totalCount).toString().getUnit()
    view.text = view.context.getString(R.string.tv_custom_price_text, totalPrice)
}

@BindingAdapter("customTotalCountText")
fun setCustomTotalCountText(view: TextView, menuMap: Map<MenuVO, Int>?) {
    val size = (menuMap?.map { it.value }?.sum() ?: 0).toString().getUnit()
    view.text = view.context.getString(R.string.tv_custom_total_count_text, size)
}

@BindingAdapter("totalPriceMap", "useMileage")
fun setCustomPayPriceText(view: TextView, totalPriceMap: Map<MenuVO, Int>?, useMileage: Stack<String>?) {
    val totalPrice = totalPriceMap?.map { it.key.price * it.value }?.sum() ?: 0
    var useMileagePrice = 0
    useMileage?.let {
        if (it.isNotEmpty()) {
            useMileagePrice = useMileage.joinToString("").toInt()
        }
    }
    view.text = view.context.getString(R.string.tv_custom_price_text, (totalPrice - useMileagePrice).toString().getUnit())
}

@BindingAdapter("customTotalPriceText")
fun setCustomTotalPriceText(view: TextView, menuMap: Map<MenuVO, Int>?) {
    val totalPrice = (menuMap?.map { it.key.price * it.value }?.sum() ?: 0).toString().getUnit()
    view.text = view.context.getString(R.string.tv_custom_price_text, totalPrice)
}

@BindingAdapter("optionListText")
fun setOptionListText(view: TextView, optionList: List<OptionVO>?) {
    val optionStr = optionList?.let { list ->
        list.joinToString { optionVO ->
            optionVO.options.joinToString { optionTypeVO ->
                optionTypeVO.name
            }
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
fun setOptionsVisibility(view: TextView, optionList: List<OptionVO>?) {
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
    val mileageStr = mileage.toString().getUnit()
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
            mileage = stack.joinToString("").getUnit()
        }
    }
    view.text = view.context.getString(R.string.tv_mileage_text, mileage)
}

@BindingAdapter("setCustomUseAllMileage")
fun setCustomUseAllMileage(view: TextView, mileage: Int) {
    val mileageStr = mileage.toString().getUnit()
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
