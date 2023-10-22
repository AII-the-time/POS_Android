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
    val createdAt: String,
    var isFocused: Boolean = false
) : BaseRecyclerViewItem()

data class MenuWithRecipeVO(
    override val id: Int,
    override val viewType: String = "MENU",
    val category: String,
    var menuId: Int? = null,
    val menuName: String,
    val price: BigDecimal,
    val option: List<OptionVO>,
    val recipe: List<RecipeVO>
) : BaseRecyclerViewItem()

@Serializable
data class MenuVO(
    override val id: Int,
    override val viewType: String = "MENU",
    val name: String,
    val price: Int,
    val option: List<OptionVO>,
    val detail: String? = null,
    var isFocused: Boolean = false
) : java.io.Serializable, BaseRecyclerViewItem()

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
) : java.io.Serializable, BaseRecyclerViewItem()

@Serializable
data class RecipeVO(
    override val id: Int,
    override val viewType: String = "RECIPE",
    var name: String,
    val isMixed: Boolean,
    var coldRegularAmount: String?,
    var unit: String
) : java.io.Serializable, BaseRecyclerViewItem() {
    constructor(stock: StockWithMixedVO): this(stock.id, "RECIPE", stock.name, stock.isMixed, null, "g")
}

data class PreorderVO(
    override val id: Int,
    override val viewType: String = "PREORDER",
    val createdAt: String,
    val orderedFor: String,
    val totalPrice: String,
    val totalCount: Int,
    val phone: String,
    val memo: String? = null,
    var isFocused: Boolean = false
) : BaseRecyclerViewItem()
