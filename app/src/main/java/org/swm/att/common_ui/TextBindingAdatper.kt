package org.swm.att.common_ui

import android.widget.TextView
import androidx.databinding.BindingAdapter

@BindingAdapter("customPriceText")
fun setCustomPriceText(view: TextView, price: Int) {
    view.text = price.toString() + "원"
}