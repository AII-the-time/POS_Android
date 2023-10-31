package org.swm.att.home.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.ListAdapter
import org.swm.att.common_ui.presenter.base.BaseRecyclerViewViewHolder
import org.swm.att.common_ui.util.ItemDiffCallback
import org.swm.att.domain.constant.BaseItemViewType
import org.swm.att.domain.entity.request.OrderedMenuVO
import org.swm.att.domain.entity.response.BaseRecyclerViewItem
import org.swm.att.domain.entity.response.OrderedMenuOfBillVO
import org.swm.att.home.R
import org.swm.att.home.databinding.ItemOrderedMenuBinding

class BaseRecyclerViewAdapter: ListAdapter<BaseRecyclerViewItem, BaseRecyclerViewViewHolder>(
    ItemDiffCallback(
        onItemsTheSame = { old, new -> old.id == new.id },
        onContentTheSame = { old, new -> old == new }
    )
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseRecyclerViewViewHolder {
        return getViewHolder(parent, BaseItemViewType.getViewTypeByOrdinal(viewType))
    }

    override fun onBindViewHolder(holder: BaseRecyclerViewViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    override fun getItemViewType(position: Int): Int {
        return BaseItemViewType.findViewTypeByString(getItem(position).viewType).ordinal
    }

    companion object {
        private fun getLayoutByViewType(viewType: BaseItemViewType): Int {
            return when(viewType) {
                BaseItemViewType.MENU_OF_BILL -> R.layout.item_ordered_menu
            }
        }

        private fun getViewHolder(viewGroup: ViewGroup, viewType: BaseItemViewType): BaseRecyclerViewViewHolder {
            val binding: ViewDataBinding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.context), getLayoutByViewType(viewType), viewGroup, false)
            return when(viewType) {
                BaseItemViewType.MENU_OF_BILL -> OrderedMenuOfBillViewHolder(binding as ItemOrderedMenuBinding)
            }
        }
    }


}

class OrderedMenuOfBillViewHolder (
    private val binding: ItemOrderedMenuBinding
) : BaseRecyclerViewViewHolder(binding) {
    override fun bind(item: BaseRecyclerViewItem) {
        val item = item as OrderedMenuOfBillVO
        binding.orderedMenu = OrderedMenuVO(
            id = item.id,
            name = item.menuName,
            price = item.price.toInt(),
            count = item.count,
            options = item.options,
            detail =  item.detail
        )
        binding.tvMenuItemPrice.visibility = View.INVISIBLE
        binding.dividerForReceiptMenuItem.visibility = View.VISIBLE
    }
}