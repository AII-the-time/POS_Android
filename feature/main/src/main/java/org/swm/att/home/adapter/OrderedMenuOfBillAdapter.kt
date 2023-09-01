package org.swm.att.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import org.swm.att.common_ui.base.BaseListAdapter
import org.swm.att.common_ui.util.ItemDiffCallback
import org.swm.att.domain.entity.request.OrderedMenuVO
import org.swm.att.domain.entity.response.OrderedMenuOfBillVO
import org.swm.att.home.databinding.ItemOrderedMenuBinding

class OrderedMenuOfBillAdapter: BaseListAdapter<OrderedMenuOfBillVO, OrderedMenuOfBillViewHolder>(
    ItemDiffCallback(
        onItemsTheSame = { old, new -> old.id == new.id },
        onContentTheSame = { old, new -> old == new }
    )
) {
    override fun inflateBinding(inflater: LayoutInflater, parent: ViewGroup): ViewDataBinding {
        return ItemOrderedMenuBinding.inflate(inflater, parent, false)
    }

    override fun createViewHolder(binding: ViewDataBinding): OrderedMenuOfBillViewHolder {
        return OrderedMenuOfBillViewHolder(binding as ItemOrderedMenuBinding)
    }

    override fun bindViewHolder(holder: OrderedMenuOfBillViewHolder, item: OrderedMenuOfBillVO) {
        holder.bind(item)
    }

}

class OrderedMenuOfBillViewHolder (
    private val binding: ItemOrderedMenuBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(orderedMenuOfBill: OrderedMenuOfBillVO) {
        binding.orderedMenu = OrderedMenuVO(
            id = orderedMenuOfBill.id,
            name = orderedMenuOfBill.menuName,
            price = orderedMenuOfBill.price.toInt(),
            count = orderedMenuOfBill.count,
            options = orderedMenuOfBill.options,
            detail =  orderedMenuOfBill.detail
        )
        binding.tvMenuItemPrice.visibility = View.INVISIBLE
    }
}