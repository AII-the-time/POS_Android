package org.swm.att.domain.entity.response

import kotlinx.serialization.Serializable
import org.swm.att.domain.constant.PayMethod
import org.swm.att.domain.constant.PayState
import org.swm.att.domain.util.BigDecimalSerializer
import java.math.BigDecimal

abstract class BaseRecyclerViewItem {
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
): BaseRecyclerViewItem()

@Serializable
data class MenuVO(
    override val id: Int,
    override val viewType: String = "MENU",
    val name: String,
    val price: Int,
    val option: List<OptionVO>,
    val recipe: List<RecipeVO>,
    val detail: String? = null
): java.io.Serializable, BaseRecyclerViewItem()

@Serializable
data class OrderedMenuOfBillVO(
    override val id: Int,
    override val viewType: String = "MENU_OF_BILL",
    val count: Int,
    @Serializable(with = BigDecimalSerializer::class)
    val price: BigDecimal,
    val menuName: String,
    val options: List<OptionTypeVO>,
    val detail: String
): java.io.Serializable, BaseRecyclerViewItem()

@Serializable
data class OptionVO(
    override val id: Int,
    override val viewType: String = "OPTION",
    val optionType: String,
    val options: List<OptionTypeVO>
): java.io.Serializable, BaseRecyclerViewItem()

@Serializable
data class RecipeVO(
    override val id: Int,
    override val viewType: String = "RECIPE",
    val name: String,
    val amount: Int,
    val unit: String
): java.io.Serializable, BaseRecyclerViewItem()