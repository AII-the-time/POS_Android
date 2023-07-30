package org.swm.att.home.home

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import org.swm.att.domain.entity.response.MenuVO
import org.swm.att.home.databinding.ItemSelectedMenuBinding

class SelectedMenuViewHolder(
    private val binding: ItemSelectedMenuBinding,
    private val homeViewModel: HomeViewModel
): RecyclerView.ViewHolder(binding.root) {

    fun bind(menu: MenuVO, count: Int) {
        Log.d("selectedMenu", "$count ${menu.name}")
        binding.menu = menu
        binding.count = count
        binding.btnPlusMenuItem.setOnClickListener {
            homeViewModel.plusSelectedMenuItem(menu)
        }
        binding.btnMinusMenuItem.setOnClickListener {
            homeViewModel.minusSelectedMenuItem(menu)
        }
        binding.btnDeleteSelectedMenuItem.setOnClickListener {
            homeViewModel.deleteSelectedMenuItem(menu)
        }
    }
}