package com.task.wikicats.adapter

import android.graphics.drawable.Drawable
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.task.wikicats.databinding.ItemCatBinding
import com.task.wikicats.model.Cat

class CatViewHolder(private val binding: ItemCatBinding) : RecyclerView.ViewHolder(binding.root) {
    val imageView = binding.ivCatImage

    fun setCat(cat: Cat) {
        updateImage(cat)
        binding.tvCatName.text = cat.name
        binding.tvCatDescription.text = cat.description
    }

    private fun updateImage(cat: Cat) {
        val thumbnail = Glide.with(binding.root).load(cat.thumbnail)

        Glide.with(binding.root)
            .load(cat.image)
            .thumbnail(thumbnail)
            .fitCenter()
            .listener(ImageRequestListener())
            .into(imageView)
    }

    private inner class ImageRequestListener : RequestListener<Drawable> {
        override fun onLoadFailed(
            e: GlideException?,
            model: Any?,
            target: Target<Drawable>?,
            isFirstResource: Boolean
        ): Boolean {
            imageView.visibility = View.GONE
            return false
        }

        override fun onResourceReady(
            resource: Drawable?,
            model: Any?,
            target: Target<Drawable>?,
            dataSource: DataSource?,
            isFirstResource: Boolean
        ): Boolean {
            imageView.visibility = View.VISIBLE
            return false
        }
    }
}