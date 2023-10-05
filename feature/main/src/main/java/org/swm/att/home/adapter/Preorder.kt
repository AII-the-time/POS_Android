package org.swm.att.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import org.swm.att.common_ui.base.BaseListAdapter
import org.swm.att.common_ui.util.ItemDiffCallback
import org.swm.att.domain.entity.response.PreorderVO
import org.swm.att.home.R
import org.swm.att.home.databinding.ItemPreorderBinding
import org.swm.att.home.preorder.PreorderViewModel

class PreorderListItemAdapter(
    private val preorderViewModel: PreorderViewModel,
    private val isValid: Boolean
): BaseListAdapter<PreorderVO, PreorderItemViewHolder>(
    ItemDiffCallback(
        onItemsTheSame = { old, new -> old.preOrderId == new.preOrderId },
        onContentTheSame = { old, new -> old == new }
    )
) {
    override fun inflateBinding(inflater: LayoutInflater, parent: ViewGroup): ViewDataBinding {
        return ItemPreorderBinding.inflate(inflater, parent, false)
    }

    override fun createViewHolder(binding: ViewDataBinding): PreorderItemViewHolder {
        return PreorderItemViewHolder(binding as ItemPreorderBinding)
    }

    override fun onBindViewHolder(holder: PreorderItemViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val item = getItem(position)
        holder.itemView.setOnClickListener {
            // 추후 api 연동 및 실제 데이터 사용
            preorderViewModel.getSelectedPreorderDetail(item.preOrderId)
            preorderViewModel.setCurrentSelectedPreorderId(position, isValid)
        }

        if (isValid && position == 0) {
            holder.itemView.setBackgroundResource(R.color.main_trans)
            preorderViewModel.getSelectedPreorderDetail(item.preOrderId)
        }
    }

    override fun bindViewHolder(holder: PreorderItemViewHolder, item: PreorderVO) {
        holder.bind(item)
    }

}

class PreorderItemViewHolder(
    private val binding: ItemPreorderBinding
): RecyclerView.ViewHolder(binding.root) {
    fun bind(preorder: PreorderVO) {
        binding.preorder = preorder
    }
}