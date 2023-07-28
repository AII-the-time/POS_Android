package org.swm.att.common_ui.util

import android.widget.TextView
import androidx.databinding.BindingAdapter
import org.swm.att.common_ui.R


@BindingAdapter("customPriceText")
fun setCustomPriceText(view: TextView, price: Int) {
    view.text = view.context.getString(R.string.tv_custom_price_text, price)
}

@BindingAdapter("price", "totalCount")
fun setCustomTotalPriceText(view: TextView, price: Int, totalCount: Int) {
    view.text = (price * totalCount).toString()
}

@BindingAdapter("customTotalCountText")
fun setCustomTotalCountText(view: TextView, menuMap: Map<org.swm.att.domain.entity.response.MenuVO, Int>?) {
    val size = menuMap?.map { it.value }?.sum() ?: 0
    view.text = view.context.getString(R.string.tv_custom_total_count_text, size)
}

@BindingAdapter("customTotalPriceText")
fun setCustomTotalPriceText(view: TextView, menuMap: Map<org.swm.att.domain.entity.response.MenuVO, Int>?) {
    val totalPrice = menuMap?.map { it.key.price * it.value }?.sum() ?: 0
    view.text = view.context.getString(R.string.tv_custom_price_text, totalPrice)
}
