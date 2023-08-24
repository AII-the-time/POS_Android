package org.swm.att.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ListAdapter
import org.swm.att.common_ui.util.ItemDiffCallback
import org.swm.att.domain.entity.response.OptionVO
import org.swm.att.home.R
import org.swm.att.home.home.option.MenuOptionViewHolder
import org.swm.att.home.home.option.MenuOptionViewModel


class MenuOptionAdapter(
    private val menuOptionViewModel: MenuOptionViewModel
): ListAdapter<OptionVO, MenuOptionViewHolder>(
    ItemDiffCallback<OptionVO>(
        onItemsTheSame = { old, new -> old.id == new.id },
        onContentTheSame = { old, new -> old == new }
    )
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuOptionViewHolder {
        return MenuOptionViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_menu_option,
                parent,
                false
            ),
            menuOptionViewModel
        )
    }

    override fun onBindViewHolder(holder: MenuOptionViewHolder, position: Int) {
        val menuOption = getItem(position)
        holder.bind(menuOption)
    }

}