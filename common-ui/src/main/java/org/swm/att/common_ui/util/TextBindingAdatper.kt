package org.swm.att.common_ui.util

import android.widget.TextView
import androidx.databinding.BindingAdapter
import org.swm.att.common_ui.R
import org.swm.att.common_ui.util.CurrencyFormat.getUnit
import org.swm.att.domain.entity.response.MenuVO

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

@BindingAdapter("customTotalPriceText")
fun setCustomTotalPriceText(view: TextView, menuMap: Map<MenuVO, Int>?) {
    val totalPrice = (menuMap?.map { it.key.price * it.value }?.sum() ?: 0).toString().getUnit()
    view.text = view.context.getString(R.string.tv_custom_price_text, totalPrice)
}
