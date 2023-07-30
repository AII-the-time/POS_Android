package org.swm.att.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ListAdapter
import org.swm.att.common_ui.constant.SelectedState
import org.swm.att.domain.entity.response.MenuVO
import org.swm.att.home.R
import org.swm.att.home.home.SelectedMenuViewHolder

class SelectedMenuAdapter : ListAdapter<Pair<MenuVO, Int>, SelectedMenuViewHolder>(
    org.swm.att.common_ui.util.ItemDiffCallback<Pair<MenuVO, Int>>(
        onItemsTheSame = { old, new -> old.first.id == new.first.id },
        //서버에서 id 넘겨줄 경우, id로 변경해야 함
        onContentTheSame = { old, new -> old == new }
    )
) {
    private var onItemClickListener: ((MenuVO, SelectedState) -> Unit)? = null
    private var onDeleteBtnClickListener: ((MenuVO) -> Unit)? = null
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
            onItemClickListener?.let { it(menu, SelectedState.PLUS_STATE) }
        }

        holder.itemView.findViewById<AppCompatButton>(R.id.btn_minus_menu_item).setOnClickListener {
            onItemClickListener?.let { it(menu, SelectedState.MINUS_STATE) }
        }

        holder.itemView.findViewById<AppCompatButton>(R.id.btn_delete_selected_menu_item).setOnClickListener {
            onDeleteBtnClickListener?.let { it(menu) }
        }
    }

    fun setOnItemClickListener(listener: (MenuVO, SelectedState) -> Unit) {
        onItemClickListener = listener
    }

    fun setOnDeleteBtnClickListener(listener: (MenuVO) -> Unit) {
        onDeleteBtnClickListener = listener
    }
}