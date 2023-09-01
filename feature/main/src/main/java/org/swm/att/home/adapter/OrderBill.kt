package org.swm.att.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import org.swm.att.common_ui.base.BaseListAdapter
import org.swm.att.common_ui.util.ItemDiffCallback
import org.swm.att.domain.entity.response.OrderBillVO
import org.swm.att.home.databinding.ItemBillBinding

class OrderBillItemAdapter: BaseListAdapter<OrderBillVO, BillViewHolder>(
    ItemDiffCallback(
        onItemsTheSame = { old, new -> old.orderId == new.orderId },
        onContentTheSame = { old, new -> old == new }
    )
) {
    override fun inflateBinding(inflater: LayoutInflater, parent: ViewGroup): ViewDataBinding {
        return ItemBillBinding.inflate(inflater, parent, false)
    }

    override fun createViewHolder(binding: ViewDataBinding): BillViewHolder {
        return BillViewHolder(binding as ItemBillBinding)
    }

    override fun bindViewHolder(holder: BillViewHolder, item: OrderBillVO) {
        holder.bind(item)
    }
}

class BillViewHolder(
    private val binding: ItemBillBinding
): RecyclerView.ViewHolder(binding.root) {
    fun bind(bill: OrderBillVO) {
        binding.bill = bill
    }
}