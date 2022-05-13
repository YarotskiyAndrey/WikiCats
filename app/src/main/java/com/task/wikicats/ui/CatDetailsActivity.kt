package com.task.wikicats.ui

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.text.Html
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.task.wikicats.databinding.ActivityCatDetailsBinding
import com.task.wikicats.model.Cat
import com.task.wikicats.ui.ErrorManager.showErrorMessage
import com.task.wikicats.viewModel.CatDetailsViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class CatDetailsActivity : AppCompatActivity() {
    private val viewModel by viewModels<CatDetailsViewModel> {
        CatDetailsViewModel.Factory(intent.getSerializableExtra(CAT_PREVIEW_EXTRA) as Cat)
    }

    private lateinit var binding: ActivityCatDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCatDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.tbToolbar)
        supportPostponeEnterTransition()
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch { viewModel.catState.collect(this@CatDetailsActivity::updateView) }
                launch { viewModel.errorFlow.collect { showErrorMessage(it) } }
            }
        }
    }

    private fun updateView(cat: Cat) {
        supportActionBar?.title = cat.name
        binding.tvCatDescription.text =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                Html.fromHtml(cat.description, Html.FROM_HTML_MODE_COMPACT)
            else
                Html.fromHtml(cat.description)

        val thumbnail = Glide.with(binding.root).load(cat.thumbnail)
        Glide.with(this)
            .load(cat.image)
            .thumbnail(thumbnail)
            .fitCenter()
            .listener(DrawableRequestListener())
            .into(binding.ivCatImage)
    }

    inner class DrawableRequestListener : RequestListener<Drawable> {
        override fun onLoadFailed(
            e: GlideException?,
            model: Any?,
            target: Target<Drawable>?,
            isFirstResource: Boolean
        ): Boolean {
            supportStartPostponedEnterTransition()
            return false
        }

        override fun onResourceReady(
            resource: Drawable?,
            model: Any?,
            target: Target<Drawable>?,
            dataSource: DataSource?,
            isFirstResource: Boolean
        ): Boolean {
            supportStartPostponedEnterTransition()
            return false
        }
    }

    companion object {
        private const val CAT_PREVIEW_EXTRA = "CAT_PREVIEW_EXTRA"
        fun newIntent(context: Context, catPreview: Cat): Intent =
            Intent(context, CatDetailsActivity::class.java)
                .putExtra(CAT_PREVIEW_EXTRA, catPreview)
                .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
    }
}