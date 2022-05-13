package com.task.wikicats.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.task.wikicats.R
import com.task.wikicats.databinding.ItemCatBinding
import com.task.wikicats.model.Cat

class CatListAdapter(private val onItemClickListener: OnItemClickListener) :
    RecyclerView.Adapter<CatViewHolder>() {

    private val _catList = ArrayList<Cat>()

    fun updateCatList(catList: List<Cat>) {
        DiffUtil.calculateDiff(CatDiffUtilCallback(_catList, catList))
            .dispatchUpdatesTo(this)

        _catList.clear()
        _catList.addAll(catList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatViewHolder {
        val binding = ItemCatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CatViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CatViewHolder, position: Int) {
        val cat = _catList[position]
        val transitionImage= holder.imageView
        holder.itemView.setOnClickListener { onItemClickListener.onItemClick(cat, transitionImage) }
        holder.setCat(cat)
    }

    override fun getItemCount() = _catList.size
}