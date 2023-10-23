package org.swm.att.common_ui.constant

import org.swm.att.common_ui.R

enum class StockState(
    val state: String,
    val icon: Int?
) {
    EMPTY("없음", R.drawable.ic_stock_none_mdpi),
    OUT_OF_STOCK("부족", R.drawable.ic_out_of_stock_mdpi),
    CAUTION("주의", R.drawable.ic_stock_caution_mdpi),
    ENOUGH("충분", R.drawable.ic_stock_enough_mdpi),
    UNKNOWN("입력해주세요", null);

    companion object {
        fun toStockState(stockState: String): StockState {
            return when (stockState) {
                "EMPTY" -> EMPTY
                "OUT_OF_STOCK" -> OUT_OF_STOCK
                "CAUTION" -> CAUTION
                "ENOUGH" -> ENOUGH
                else -> UNKNOWN
            }
        }
    }
}