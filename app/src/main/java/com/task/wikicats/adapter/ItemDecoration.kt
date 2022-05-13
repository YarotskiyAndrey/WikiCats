package com.task.wikicats.adapter

import android.graphics.Rect
import android.view.View
import androidx.annotation.DimenRes
import androidx.recyclerview.widget.RecyclerView

class ItemDecoration(@DimenRes private val offsetRes: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildLayoutPosition(view);
        val size = parent.adapter?.itemCount ?: 0

        val offset = view.resources.getDimension(offsetRes).toInt()

        outRect.right = offset
        outRect.top = offset
        outRect.left = offset
        if (position + 1 == size) outRect.bottom = offset
    }
}