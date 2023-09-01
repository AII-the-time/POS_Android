package org.swm.att.common_ui.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

abstract class BaseListAdapter<T, ViewHolder: RecyclerView.ViewHolder>(
    diffCallback: DiffUtil.ItemCallback<T>
): ListAdapter<T, ViewHolder>(diffCallback) {
    lateinit var binding: ViewDataBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = inflateBinding(inflater, parent)
        return createViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        bindViewHolder(holder, item)
    }

    abstract fun inflateBinding(inflater: LayoutInflater, parent: ViewGroup): ViewDataBinding

    abstract fun createViewHolder(binding: ViewDataBinding): ViewHolder

    abstract fun bindViewHolder(holder: ViewHolder, item: T)

}