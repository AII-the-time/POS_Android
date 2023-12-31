package org.swm.att.domain.constant

import org.swm.att.domain.entity.response.MenuVO
import org.swm.att.domain.entity.response.OrderBillVO
import org.swm.att.domain.entity.response.PreorderVO
import org.swm.att.domain.entity.response.StockWithStateVO
import java.lang.reflect.Type

enum class SelectedItemViewType(
    viewClassType: Type
) {
    BILL(OrderBillVO::class.java),
    MENU(MenuVO::class.java),
    PREORDER(PreorderVO::class.java),
    STOCK_WITH_STATE(StockWithStateVO::class.java);

    companion object {
        fun getViewTypeByOrdinal(ordinalNum: Int): SelectedItemViewType {
            return values()[ordinalNum]
        }

        fun findViewTypeByString(strViewType: String): SelectedItemViewType {
            return values().find { it.name == strViewType } ?: BILL
        }
    }
}