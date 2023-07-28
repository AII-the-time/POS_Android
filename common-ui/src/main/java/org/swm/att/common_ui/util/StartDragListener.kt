package org.swm.att.common_ui.util

import androidx.recyclerview.widget.RecyclerView

interface StartDragListener {
    fun onStartDrag(viewHolder: RecyclerView.ViewHolder)
}