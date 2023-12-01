package com.dicoding.appstorysub2.ui.addstory

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.dicoding.appstorysub2.data.remote.request.AddStoryRequest
import com.dicoding.appstorysub2.data.remote.response.AddStoryResponse
import com.dicoding.appstorysub2.data.repository.story.StoryRepository
import com.dicoding.appstorysub2.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.File
import javax.inject.Inject

@HiltViewModel
class AddStoryViewModel @Inject constructor(
    private val storyRepository: StoryRepository,
) : ViewModel() {

    fun addNewStory(photo: File, description: String, location: Location?): LiveData<Result<AddStoryResponse>> {
        val addStoryRequest = AddStoryRequest(photo, description, location)
        return storyRepository.addNewStory(addStoryRequest).asLiveData()
    }
}