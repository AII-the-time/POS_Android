package org.swm.att.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import org.swm.att.common_ui.base.BaseListAdapter
import org.swm.att.domain.entity.request.OrderedMenuVO
import org.swm.att.home.databinding.ItemSelectedMenuBinding
import org.swm.att.home.home.HomeViewModel
import org.swm.att.home.home.SelectedMenuViewHolder

class SelectedMenuAdapter(
    private val homeViewModel: HomeViewModel
) : BaseListAdapter<Pair<OrderedMenuVO, Int>, SelectedMenuViewHolder>(
    org.swm.att.common_ui.util.ItemDiffCallback<Pair<OrderedMenuVO, Int>>(
        onItemsTheSame = { old, new -> old.first.id == new.first.id && old.first.options == new.first.options },
        onContentTheSame = { old, new -> old == new }
    )
) {

    override fun inflateBinding(inflater: LayoutInflater, parent: ViewGroup): ViewDataBinding {
        binding = ItemSelectedMenuBinding.inflate(inflater, parent, false)
        (binding as ItemSelectedMenuBinding).homeViewModel = homeViewModel
        return binding
    }

    override fun createViewHolder(binding: ViewDataBinding): SelectedMenuViewHolder {
        return SelectedMenuViewHolder(binding as ItemSelectedMenuBinding)
    }

    override fun bindViewHolder(holder: SelectedMenuViewHolder, item: Pair<OrderedMenuVO, Int>) {
        holder.bind(item.first, item.second)
    }

}