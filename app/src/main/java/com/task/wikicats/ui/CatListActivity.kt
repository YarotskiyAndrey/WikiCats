package com.task.wikicats.ui

import android.app.ActivityOptions
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.task.wikicats.R
import com.task.wikicats.adapter.CatListAdapter
import com.task.wikicats.adapter.ItemDecoration
import com.task.wikicats.adapter.OnItemClickListener
import com.task.wikicats.databinding.ActivityCatListBinding
import com.task.wikicats.model.Cat
import com.task.wikicats.ui.ErrorManager.showErrorMessage
import com.task.wikicats.viewModel.CatListViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class CatListActivity : AppCompatActivity(), OnItemClickListener {

    private val viewModel: CatListViewModel by viewModels { CatListViewModel.Factory() }
    private lateinit var catListAdapter: CatListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityCatListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        catListAdapter = CatListAdapter(this)
        binding.rvCatList.addItemDecoration(ItemDecoration(R.dimen.item_cat_offset))
        binding.rvCatList.layoutManager = LinearLayoutManager(this)
        binding.rvCatList.adapter = catListAdapter

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch { viewModel.catListState.collect(catListAdapter::updateCatList) }
                launch { viewModel.errorFlow.collect { showErrorMessage(it) } }
            }
        }
    }

    override fun onItemClick(cat: Cat, transitionImage: ImageView) {
        val intent = CatDetailsActivity.newIntent(this, cat)
        val activityOptions = ActivityOptions
            .makeSceneTransitionAnimation(this, transitionImage, transitionImage.transitionName)
        startActivity(intent, activityOptions.toBundle())
    }
}