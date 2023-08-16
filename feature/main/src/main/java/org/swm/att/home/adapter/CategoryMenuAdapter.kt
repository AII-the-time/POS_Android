package org.swm.att.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.ListAdapter
import org.swm.att.common_ui.util.StartDragListener
import org.swm.att.domain.entity.response.MenuVO
import org.swm.att.home.R
import org.swm.att.home.option.MenuOptionDialog
import org.swm.att.home.home.HomeViewModel
import org.swm.att.home.menu.MenuViewHolder

class CategoryMenuAdapter(
    private val homeViewModel: HomeViewModel,
    private val parentContext: Context
) : ListAdapter<MenuVO, MenuViewHolder>(
    org.swm.att.common_ui.util.ItemDiffCallback<MenuVO>(
        onItemsTheSame = { old, new -> old.id == new.id },
        onContentTheSame = { old, new -> old == new }
    )
), org.swm.att.common_ui.util.ItemTouchHelperListener {
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
            if (menu.option.isNotEmpty()) {
                val menuOptionDialog = MenuOptionDialog(homeViewModel, menu)
                menuOptionDialog.show(
                    (parentContext as FragmentActivity).supportFragmentManager,
                    MenuOptionDialog::class.java.simpleName
                )
            } else {
                homeViewModel.addSelectedMenu(menu)
            }
        }
        holder.itemView.setOnTouchListener { _, motionEvent ->
            if (motionEvent.actionMasked == android.view.MotionEvent.ACTION_DOWN) {
                onItemDragListener.onStartDrag(holder)
            }
            false
        }
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