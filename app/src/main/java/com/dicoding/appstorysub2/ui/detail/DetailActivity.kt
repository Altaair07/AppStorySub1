package com.dicoding.appstorysub2.ui.detail

import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.dicoding.appstorysub2.data.remote.response.StoryItemResponse
import com.dicoding.appstorysub2.databinding.ActivityDetailBinding
import com.dicoding.appstorysub2.util.Result
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityDetailBinding.inflate(layoutInflater)
    }
    private val viewModel by viewModels<DetailViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val story = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(EXTRA_STORY, StoryItemResponse::class.java)
        } else {
            intent.getParcelableExtra(EXTRA_STORY)
        }

        with(binding) {
            toolbar.title = story?.name
            setSupportActionBar(toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        viewModel.getDetailStory(story?.id.toString()).observe(this) { result ->
            when (result) {
                is Result.Error -> {
                    showLoading(false)
                    Snackbar.make(binding.root, result.message.toString(), Snackbar.LENGTH_SHORT).show()
                }
                is Result.Loading -> showLoading(true)
                is Result.Success -> {
                    showLoading(false)
                    populateDetail(result.data)
                }
            }
        }
    }

    private fun populateDetail(story: StoryItemResponse) {
        with(binding) {
            Glide.with(this@DetailActivity)
                .load(story.photoUrl)
                .into(ivDetailPhoto)

            tvDetailDescription.text = story.description
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    private fun showLoading(isLoading: Boolean) {
        with(binding) {
            pbLoading.isVisible = isLoading
        }
    }

    companion object {
        const val EXTRA_STORY = "extra_story"
    }
}