package org.swm.att.presenter.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ListAdapter
import org.swm.att.R
import toss.next.common_ui.util.ItemDiffCallback
import toss.next.common_ui.util.ItemTouchHelperListener
import toss.next.common_ui.util.StartDragListener
import org.swm.att.domain.entity.response.MenuVO
import org.swm.att.presenter.menu.MenuViewHolder

class CategoryMenuAdapter: ListAdapter<org.swm.att.domain.entity.response.MenuVO, MenuViewHolder>(
    toss.next.common_ui.util.ItemDiffCallback<MenuVO>(
        onItemsTheSame = { old, new -> old == new },
        //서버에서 id 넘겨줄 경우, id로 변경해야 함
        onContentTheSame = { old, new -> old == new }
    )
), toss.next.common_ui.util.ItemTouchHelperListener {
    private var onItemClickListener: ((org.swm.att.domain.entity.response.MenuVO) -> Unit)? = null
    private lateinit var onItemDragListener: toss.next.common_ui.util.StartDragListener

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
        holder.itemView.setOnTouchListener { _, motionEvent ->
            if (motionEvent.actionMasked == android.view.MotionEvent.ACTION_DOWN) {
                onItemDragListener.onStartDrag(holder)
            }
            false
        }
    }

    fun setOnItemClickListener(listener: (org.swm.att.domain.entity.response.MenuVO) -> Unit) {
        onItemClickListener = listener
    }

    fun setOnStartDragListener(listener: toss.next.common_ui.util.StartDragListener) {
        onItemDragListener = listener
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
        val item = getItem(fromPosition)
        val currentList = currentList.toMutableList()

        currentList.removeAt(fromPosition)
        currentList.add(toPosition, item)
        submitList(currentList)
        return true
    }

}