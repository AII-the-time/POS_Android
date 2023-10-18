package org.swm.att.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import org.swm.att.common_ui.presenter.base.BaseListAdapter
import org.swm.att.common_ui.util.ItemDiffCallback
import org.swm.att.domain.entity.response.OptionVO
import org.swm.att.home.databinding.ItemMenuOptionBinding
import org.swm.att.home.home.option.MenuOptionViewHolder
import org.swm.att.home.home.option.MenuOptionViewModel


class MenuOptionAdapter(
    private val menuOptionViewModel: MenuOptionViewModel
): BaseListAdapter<OptionVO, MenuOptionViewHolder>(
    ItemDiffCallback(
        onItemsTheSame = { old, new -> old.id == new.id },
        onContentTheSame = { old, new -> old == new }
    )
) {
    override fun inflateBinding(inflater: LayoutInflater, parent: ViewGroup): ViewDataBinding {
        return ItemMenuOptionBinding.inflate(inflater, parent, false)
    }
    override fun createViewHolder(binding: ViewDataBinding): MenuOptionViewHolder {
        return MenuOptionViewHolder(
            binding as ItemMenuOptionBinding,
            menuOptionViewModel
        )
    }
    override fun bindViewHolder(holder: MenuOptionViewHolder, item: OptionVO) {
        holder.bind(item)
    }
}