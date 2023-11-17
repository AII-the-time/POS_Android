package org.swm.att.common_ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import org.swm.att.common_ui.util.ItemDiffCallback
import org.swm.att.common_ui.viewholder.BaseSduiViewHolder
import org.swm.att.common_ui.viewholder.getSduiViewHolder
import org.swm.att.domain.entity.response.SduiReportItemVO
import org.swm.att.domain.sever_driven_ui.SduiViewType

class SduiReportAdapter: ListAdapter<SduiReportItemVO, BaseSduiViewHolder>(
    ItemDiffCallback<SduiReportItemVO>(
        onContentTheSame = { old, new -> old == new },
        onItemsTheSame = { old, new -> old.content == new.content }
    )
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseSduiViewHolder {
         return getSduiViewHolder(parent, SduiViewType.getViewTypeByOrdinal(viewType))
    }

    override fun onBindViewHolder(holder: BaseSduiViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item.content)
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).viewType.ordinal
    }
}