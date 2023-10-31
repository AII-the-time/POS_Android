package org.swm.att.home.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.ListAdapter
import org.swm.att.common_ui.util.ItemDiffCallback
import org.swm.att.common_ui.util.ItemTouchHelperListener
import org.swm.att.common_ui.util.StartDragListener
import org.swm.att.domain.entity.response.MenuVO
import org.swm.att.home.R
import org.swm.att.home.main.home.HomeViewModel
import org.swm.att.home.main.home.menu.MenuViewHolder
import org.swm.att.home.main.home.option.MenuOptionDialog

class CategoryMenuAdapter (
    private val homeViewModel: HomeViewModel
) : ListAdapter<MenuVO, MenuViewHolder>(
    ItemDiffCallback<MenuVO>(
        onItemsTheSame = { old, new -> old.id == new.id },
        onContentTheSame = { old, new -> old == new }
    )
), ItemTouchHelperListener {
    private lateinit var onItemDragListener: StartDragListener

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
            val menuOptionDialog = MenuOptionDialog(homeViewModel, menu.id)
            menuOptionDialog.show((holder.itemView.context as FragmentActivity).supportFragmentManager, "MenuOptionDialog")
        }
//        holder.itemView.setOnTouchListener { _, motionEvent ->
//            if (motionEvent.actionMasked == android.view.MotionEvent.ACTION_DOWN) {
//                onItemDragListener.onStartDrag(holder)
//            }
//            false
//        }
    }

    fun setOnStartDragListener(listener: StartDragListener) {
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