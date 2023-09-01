package org.swm.att.home.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ListAdapter
import org.swm.att.domain.entity.request.OrderedMenuVO
import org.swm.att.home.R
import org.swm.att.home.databinding.ItemSelectedMenuBinding
import org.swm.att.home.home.HomeViewModel
import org.swm.att.home.home.SelectedMenuViewHolder

class SelectedMenuAdapter(
    private val homeViewModel: HomeViewModel
) : ListAdapter<Pair<OrderedMenuVO, Int>, SelectedMenuViewHolder>(
    org.swm.att.common_ui.util.ItemDiffCallback<Pair<OrderedMenuVO, Int>>(
        onItemsTheSame = { old, new -> old.first.id == new.first.id && old.first.options == new.first.options },
        onContentTheSame = { old, new -> old == new }
    )
) {
    private lateinit var binding: ItemSelectedMenuBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectedMenuViewHolder {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_selected_menu,
            parent,
            false
        )
        binding.homeViewModel = homeViewModel
        return SelectedMenuViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SelectedMenuViewHolder, position: Int) {
        Log.d("SelectedMenuAdapter", "onBindViewHolder: ${getItem(position)}")
        val menu = getItem(position).first
        val count = getItem(position).second
        holder.bind(menu, count)
    }

}