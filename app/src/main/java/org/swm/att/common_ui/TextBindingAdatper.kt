package org.swm.att.common_ui

import android.util.Log
import android.widget.TextView
import androidx.databinding.BindingAdapter
import org.swm.att.domain.entity.response.MenuVO

@BindingAdapter("customPriceText")
fun setCustomPriceText(view: TextView, price: Int) {
    view.text = "${price}원"
}

@BindingAdapter("price", "totalCount")
fun setCustomTotalPriceText(view: TextView, price: Int, totalCount: Int) {
    view.text = (price * totalCount).toString()
}

@BindingAdapter("customTotalCountText")
fun setCustomTotalCountText(view: TextView, menuMap: Map<MenuVO, Int>?) {
    Log.d("setCustomTotalCountText", menuMap.toString())
    val size = menuMap?.size ?: 0
    view.text = "총 ${size}개"
}

@BindingAdapter("customTotalPriceText")
fun setCustomTotalPriceText(view: TextView, menuMap: Map<MenuVO, Int>?) {
    Log.d("setCustomTotalPriceText", menuMap.toString())
    val totalPrice = menuMap?.map { it.key.price * it.value }?.sum() ?: 0
    view.text = "${totalPrice}원"
}
