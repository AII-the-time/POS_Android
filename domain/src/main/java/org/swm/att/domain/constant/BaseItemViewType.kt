package org.swm.att.domain.constant

import org.swm.att.domain.entity.response.OrderedMenuOfBillVO
import org.swm.att.domain.entity.response.RecipeVO
import java.lang.reflect.Type

enum class BaseItemViewType(
    viewClassType: Type
) {
    MENU_OF_BILL(OrderedMenuOfBillVO::class.java);

    companion object {
        fun getViewTypeByOrdinal(ordinalNum: Int): BaseItemViewType {
            return values()[ordinalNum]
        }

        fun findViewTypeByString(strViewType: String): BaseItemViewType {
            return values().find { it.name == strViewType } ?: MENU_OF_BILL
        }
    }
}