package org.swm.att.common_ui

import androidx.recyclerview.widget.DiffUtil

class ItemDiffCallback<T: Any>(
    val onItemsTheSame: (T, T) -> Boolean,
    val onContentTheSame: (T, T) -> Boolean
): DiffUtil.ItemCallback<T>() {

    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean = onItemsTheSame(oldItem, newItem)

    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean = onContentTheSame(oldItem, newItem)

}