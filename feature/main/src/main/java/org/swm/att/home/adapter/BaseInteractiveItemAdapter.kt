package org.swm.att.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.ListAdapter
import org.swm.att.common_ui.presenter.base.BaseInteractiveViewHolder
import org.swm.att.common_ui.util.ItemDiffCallback
import org.swm.att.domain.constant.BaseInteractiveViewType
import org.swm.att.domain.entity.response.BaseRecyclerViewItem
import org.swm.att.home.R
import org.swm.att.home.databinding.ItemMenuOptionBinding
import org.swm.att.home.databinding.ItemMenuRecipeBinding
import org.swm.att.home.recipe.MenuRecipeViewHolder
import org.swm.att.home.recipe.MenuSelectedOptionViewHolder
import org.swm.att.home.recipe.RecipeViewModel

class BaseInteractiveItemAdapter(
    private val viewModel: ViewModel
): ListAdapter<BaseRecyclerViewItem, BaseInteractiveViewHolder> (
    ItemDiffCallback(
        onItemsTheSame = { old, new -> old.id == new.id },
        onContentTheSame = { old, new -> old == new }
    )
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseInteractiveViewHolder {
        return getViewHolder(parent, BaseInteractiveViewType.getViewTypeByOrdinal(viewType))
    }

    override fun onBindViewHolder(holder: BaseInteractiveViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    override fun getItemViewType(position: Int): Int {
        return BaseInteractiveViewType.findViewTypeByString(getItem(position).viewType).ordinal
    }

    private fun getViewHolder(viewGroup: ViewGroup, viewType: BaseInteractiveViewType): BaseInteractiveViewHolder {
        val binding: ViewDataBinding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.context), getLayoutByViewType(viewType), viewGroup, false)
        return when(viewType) {
            BaseInteractiveViewType.RECIPE -> MenuRecipeViewHolder(
                binding as ItemMenuRecipeBinding,
                viewModel as RecipeViewModel
            )

            BaseInteractiveViewType.OPTION -> MenuSelectedOptionViewHolder(
                binding as ItemMenuOptionBinding,
                viewModel as RecipeViewModel
            )
        }
    }

    companion object {
        private fun getLayoutByViewType(viewType: BaseInteractiveViewType): Int {
            return when(viewType) {
                BaseInteractiveViewType.RECIPE -> R.layout.item_menu_recipe
                BaseInteractiveViewType.OPTION -> R.layout.item_menu_option
            }
        }
    }
}