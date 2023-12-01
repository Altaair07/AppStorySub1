package com.dicoding.appstorysub2.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.dicoding.appstorysub2.data.remote.response.StoryItemResponse
import com.dicoding.appstorysub2.data.repository.story.StoryRepository
import com.dicoding.appstorysub2.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val storyRepository: StoryRepository,
) : ViewModel() {

    fun getDetailStory(id: String): LiveData<Result<StoryItemResponse>> {
        return storyRepository.getDetailStory(id).asLiveData()
    }
}