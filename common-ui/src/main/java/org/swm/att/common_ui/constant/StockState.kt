package org.swm.att.common_ui.constant

import org.swm.att.common_ui.R

enum class StockState(
    val state: String,
    val icon: Int?,
    val drawable: Int
) {
    EMPTY("없음", R.drawable.ic_out_of_stock_mdpi, R.drawable.shape_round_out_of_stock),
    OUT_OF_STOCK("부족", R.drawable.ic_out_of_stock_mdpi, R.drawable.shape_round_out_of_stock),
    CAUTION("주의", R.drawable.ic_stock_caution_mdpi, R.drawable.shape_round_stock_caution),
    ENOUGH("충분", R.drawable.ic_stock_enough_mdpi, R.drawable.shape_round_stock_enough),
    UNKNOWN("입력해주세요", null, R.drawable.shape_round_stock_none);

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