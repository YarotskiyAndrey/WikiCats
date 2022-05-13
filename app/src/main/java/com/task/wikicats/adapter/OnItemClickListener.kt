package com.task.wikicats.adapter

import android.widget.ImageView
import com.task.wikicats.model.Cat

interface OnItemClickListener {
    fun onItemClick(cat: Cat, transitionImage: ImageView)
}