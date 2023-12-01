package com.dicoding.appstorysub2.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.appstorysub2.R
import com.dicoding.appstorysub2.data.remote.response.StoryItemResponse
import com.dicoding.appstorysub2.databinding.ActivityMainBinding
import com.dicoding.appstorysub2.ui.adapter.LoadingStateAdapter
import com.dicoding.appstorysub2.ui.adapter.MainAdapter
import com.dicoding.appstorysub2.ui.addstory.AddStoryActivity
import com.dicoding.appstorysub2.ui.detail.DetailActivity
import com.dicoding.appstorysub2.ui.mapsstory.MapsStoryActivity
import com.dicoding.appstorysub2.ui.splash.RoutingActivity
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val mainAdapter = MainAdapter(
            onItemClick = { story, ivItemPhoto, tvItemName ->
                navigateToDetail(story, ivItemPhoto, tvItemName)
            }
        )
        val footerAdapter = LoadingStateAdapter {
            mainAdapter.retry()
        }

        mainAdapter.addLoadStateListener {
            if (it.source.refresh is LoadState.NotLoading && it.append.endOfPaginationReached && mainAdapter.itemCount < 1) {
                Snackbar.make(binding.root, getString(R.string.empty), Snackbar.LENGTH_SHORT).show()
            }
        }

        with(binding.rvStory) {
            layoutManager = LinearLayoutManager(this@MainActivity)
            setHasFixedSize(true)
            adapter = mainAdapter.withLoadStateFooter(footerAdapter)
        }

        with(binding) {
            setSupportActionBar(toolbar)
            swipeRefresh.setOnRefreshListener {
                swipeRefresh.isRefreshing = true
                viewModel.refresh()
            }
            btnAdd.setOnClickListener {
                val intent = Intent(this@MainActivity, AddStoryActivity::class.java)
                launcherAddStory.launch(intent)
            }
            btnMaps.setOnClickListener {
                val intent = Intent(this@MainActivity, MapsStoryActivity::class.java)
                startActivity(intent)
            }
        }

        viewModel.getAllStory().observe(this) { result ->
            hideLoading()
            mainAdapter.submitData(lifecycle, result)
        }
    }

    private fun navigateToDetail(story: StoryItemResponse, ivItemPhoto: ImageView, tvItemName: TextView) {
        val intent = Intent(this, DetailActivity::class.java).apply {
            putExtra(DetailActivity.EXTRA_STORY, story)
        }
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
            this,
            Pair(ivItemPhoto, "photo"),
            Pair(tvItemName, "name")
        )
        startActivity(intent, options.toBundle())
    }

    private fun hideLoading() {
        with(binding) {
            pbLoading.isVisible = false
            swipeRefresh.isRefreshing = false
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> logout()
            else -> super.onOptionsItemSelected(item)
        }
    }

    private val launcherAddStory = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            viewModel.refresh()
        }
    }

    private fun logout(): Boolean {
        viewModel.logout()
        val intent = Intent(this, RoutingActivity::class.java)
        startActivity(intent)
        finish()
        return true
    }
}