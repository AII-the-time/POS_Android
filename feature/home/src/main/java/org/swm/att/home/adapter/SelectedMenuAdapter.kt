package org.swm.att.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ListAdapter
import org.swm.att.domain.entity.response.MenuVO
import org.swm.att.home.R
import org.swm.att.home.home.HomeViewModel
import org.swm.att.home.home.SelectedMenuViewHolder

class SelectedMenuAdapter(
    private val homeViewModel: HomeViewModel
) : ListAdapter<Pair<MenuVO, Int>, SelectedMenuViewHolder>(
    org.swm.att.common_ui.util.ItemDiffCallback<Pair<MenuVO, Int>>(
        onItemsTheSame = { old, new -> old.first.id == new.first.id },
        onContentTheSame = { old, new -> old == new }
    )
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectedMenuViewHolder {
        return SelectedMenuViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_selected_menu,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SelectedMenuViewHolder, position: Int) {
        val menu = getItem(position).first
        val count = getItem(position).second
        holder.bind(menu, count)

        holder.itemView.findViewById<AppCompatButton>(R.id.btn_plus_menu_item).setOnClickListener {
            homeViewModel.plusSelectedMenuItem(menu)
        }

        holder.itemView.findViewById<AppCompatButton>(R.id.btn_minus_menu_item).setOnClickListener {
            homeViewModel.minusSelectedMenuItem(menu)
        }

        holder.itemView.findViewById<AppCompatButton>(R.id.btn_delete_selected_menu_item).setOnClickListener {
            homeViewModel.deleteSelectedMenuItem(menu)
        }
    }

}