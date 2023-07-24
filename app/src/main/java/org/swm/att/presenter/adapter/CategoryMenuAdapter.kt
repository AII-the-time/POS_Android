package org.swm.att.presenter.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ListAdapter
import org.swm.att.R
import org.swm.att.common_ui.ItemDiffCallback
import org.swm.att.domain.entity.response.MenuVO
import org.swm.att.presenter.menu.MenuViewHolder

class CategoryMenuAdapter: ListAdapter<MenuVO, MenuViewHolder>(
    ItemDiffCallback<MenuVO>(
        onItemsTheSame = { old, new -> old == new },
        //서버에서 id 넘겨줄 경우, id로 변경해야 함
        onContentTheSame = { old, new -> old == new }
    )
) {
    private var onItemClickListener: ((MenuVO) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        return MenuViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_menu,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val menu = getItem(position)
        holder.bind(menu)
        holder.itemView.setOnClickListener {
            onItemClickListener?.let { it(menu) }
        }
    }

    fun setOnItemClickListener(listener: (MenuVO) -> Unit) {
        onItemClickListener = listener
    }

}