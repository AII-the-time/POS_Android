package org.swm.att.domain.constant

import org.swm.att.domain.entity.response.OptionVO
import org.swm.att.domain.entity.response.RecipeVO
import java.lang.reflect.Type

enum class BaseInteractiveViewType(
    viewClassType: Type
) {
    RECIPE(RecipeVO::class.java),
    OPTION(OptionVO::class.java);

    companion object {
        fun getViewTypeByOrdinal(ordinalNum: Int): BaseInteractiveViewType {
            return BaseInteractiveViewType.values()[ordinalNum]
        }

        fun findViewTypeByString(strViewType: String): BaseInteractiveViewType {
            return BaseInteractiveViewType.values()
                .find { it.name == strViewType } ?: RECIPE
        }
    }
}