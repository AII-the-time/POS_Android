package org.swm.att.domain.entity.response

import kotlinx.serialization.Serializable
import org.swm.att.domain.constant.PayMethod
import org.swm.att.domain.constant.PayState
import java.math.BigDecimal

abstract class BaseSelectableItem {
    abstract val id: Int
    abstract val viewType: String
}

data class OrderBillVO(
    override val id: Int,
    override val viewType: String = "BILL",
    val paymentState: PayState,
    val paymentMethod: PayMethod,
    val totalCount: Int,
    val totalPrice: BigDecimal,
    val createdAt: String
): BaseSelectableItem()

@Serializable
data class MenuVO(
    override val id: Int,
    override val viewType: String = "MENU",
    val name: String,
    val price: Int,
    val option: List<OptionVO>,
    val detail: String? = null
): java.io.Serializable, BaseSelectableItem()

