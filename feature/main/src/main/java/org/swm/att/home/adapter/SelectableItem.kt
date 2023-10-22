package org.swm.att.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.ListAdapter
import org.swm.att.common_ui.presenter.base.BaseRecyclerViewViewHolder
import org.swm.att.common_ui.presenter.base.BaseSelectableViewViewModel
import org.swm.att.common_ui.util.ItemDiffCallback
import org.swm.att.domain.constant.SelectedItemViewType
import org.swm.att.domain.entity.response.BaseRecyclerViewItem
import org.swm.att.domain.entity.response.MenuVO
import org.swm.att.domain.entity.response.OrderBillVO
import org.swm.att.domain.entity.response.PreorderVO
import org.swm.att.domain.entity.response.StockWithStateVO
import org.swm.att.home.R
import org.swm.att.home.databinding.ItemBillBinding
import org.swm.att.home.databinding.ItemPreorderBinding
import org.swm.att.home.databinding.ItemRegisteredMenuBinding
import org.swm.att.home.databinding.ItemStockBinding

class SelectableItemAdapter(
    private val viewModel: BaseSelectableViewViewModel
): ListAdapter<BaseRecyclerViewItem, BaseRecyclerViewViewHolder>(
    ItemDiffCallback(
        onItemsTheSame = { old, new -> old.id == new.id },
        onContentTheSame = { old, new -> old == new }
    )
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseRecyclerViewViewHolder {
        return getViewHolder(parent, SelectedItemViewType.getViewTypeByOrdinal(viewType))
    }

    override fun onBindViewHolder(holder: BaseRecyclerViewViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
        holder.itemView.setOnClickListener {
            viewModel.setCurrentSelectedItemId(position)
            viewModel.getSelectedItem(item.id)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return SelectedItemViewType.findViewTypeByString(getItem(position).viewType).ordinal
    }

    companion object {
        private fun getLayoutByViewType(viewType: SelectedItemViewType): Int {
            return when(viewType) {
                SelectedItemViewType.BILL -> R.layout.item_bill
                SelectedItemViewType.MENU -> R.layout.item_registered_menu
                SelectedItemViewType.PREORDER -> R.layout.item_preorder
                SelectedItemViewType.STOCK_WITH_STATE -> R.layout.item_stock
            }
        }

        fun getViewHolder(viewGroup: ViewGroup, viewType: SelectedItemViewType): BaseRecyclerViewViewHolder {
            val binding: ViewDataBinding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.context), getLayoutByViewType(viewType), viewGroup, false)
            return when(viewType) {
                SelectedItemViewType.BILL -> BillViewHolder(binding as ItemBillBinding)
                SelectedItemViewType.MENU -> RegisteredMenuVIewHolder(binding as ItemRegisteredMenuBinding)
                SelectedItemViewType.PREORDER -> PreorderItemViewHolder(binding as ItemPreorderBinding)
                SelectedItemViewType.STOCK_WITH_STATE -> StockWithStateViewHolder(binding as ItemStockBinding)
            }
        }
    }
}

class BillViewHolder(
    private val binding: ItemBillBinding
): BaseRecyclerViewViewHolder(binding) {
    override fun bind(item: BaseRecyclerViewItem) {
        binding.bill = item as OrderBillVO
    }
}

class RegisteredMenuVIewHolder(
    private val binding: ItemRegisteredMenuBinding
) : BaseRecyclerViewViewHolder(binding) {
    override fun bind(item: BaseRecyclerViewItem) {
        binding.menu = item as MenuVO
    }
}

class PreorderItemViewHolder(
    private val binding: ItemPreorderBinding
) : BaseRecyclerViewViewHolder(binding) {
    override fun bind(item: BaseRecyclerViewItem) {
        binding.preorder = item as PreorderVO
    }
}

class StockWithStateViewHolder(
    private val binding: ItemStockBinding
) : BaseRecyclerViewViewHolder(binding) {
    override fun bind(item: BaseRecyclerViewItem) {
        binding.stockWithState = item as StockWithStateVO
    }
}