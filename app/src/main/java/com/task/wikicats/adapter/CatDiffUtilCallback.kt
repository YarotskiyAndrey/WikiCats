package com.task.wikicats.adapter

import androidx.recyclerview.widget.DiffUtil
import com.task.wikicats.model.Cat

class CatDiffUtilCallback(private val oldList: List<Cat>, private val newList: List<Cat>) :
    DiffUtil.Callback() {
    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldList[oldItemPosition].id == newList[oldItemPosition].id

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldList[oldItemPosition] == newList[oldItemPosition]
}